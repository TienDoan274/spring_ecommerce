import pandas as pd
from dotenv import load_dotenv
import requests
import os
import numpy as np
import json 
from rag.retrieve import combine_results,search_elasticsearch,search_close_name
from dotenv import load_dotenv
from models import PhoneRequirements,LaptopRequirements,ComparisionInput,ProductInform, Complain
from llama_index.llms.google_genai import GoogleGenAI
from .prompts import *
load_dotenv()
llm = GoogleGenAI(
    model="gemini-2.0-flash",
    api_key=os.getenv('GOOGLE_API_KEY')
)
load_dotenv()
from .prompts import PRODUCT_CONSULTATION_TEMPLATE
# openai_key = os.getenv("OPENAI_API_KEY")
# client = OpenAI(api_key=openai_key)

# def get_embedding(text: str) -> list[float]:
#     response = client.embeddings.create(
#         input=text,
#         model="text-embedding-3-small"
#     )

#     return response.data[0].embedding

def product_consultation_tool(device: str, query: str, k: int = 5) -> str:
    base_path = "/home/kltn2025/spring_ecommerce/chatbot/filtered_products"
    
    # Chọn schema và prompt theo loại thiết bị
    if device == "phone":
        reqs = llm.structured_predict(PhoneRequirements, PHONE_CONSULTATION_TEMPLATE, query=query)
        device_path = f"{base_path}/phone"
        requirements_map = {
            "gaming": ("gamecauhinhcao.csv", "gaming_rank"),
            "battery": ("pinkhung.csv", "battery_rank"),
            "camera": ("chupanhquayfim.csv", "camera_rank"),
            "streaming": ("livestream.csv", "streaming_rank"),
            "lightweight": ("mongnhe.csv", "lightweight_rank")
        }
    elif device == "laptop":
        reqs = llm.structured_predict(LaptopRequirements, LAPTOP_CONSULTATION_TEMPLATE, query=query)
        device_path = f"{base_path}/laptop"
        requirements_map = {
            "ai_capable": ("ai.csv", "ai_rank"),
            "gaming": ("gaming.csv", "gaming_rank"),
            "office": ("hoctapvanphong.csv", "office_rank"),
            "graphics": ("graphics_laptops.csv", "graphics_rank"),
            "engineering": ("kythuat.csv", "engineering_rank"),
            "lightweight": ("mongnhe.csv", "lightweight_rank"),
            "premium": ("premium_laptops.csv", "premium_rank")
        }
    else:
        return "Loại thiết bị không được hỗ trợ. Hiện tại chỉ hỗ trợ 'phone' và 'laptop'."

    # Lấy thông tin chung
    brand = reqs.brand_preference or "không xác định"
    min_budget = reqs.min_budget
    max_budget = reqs.max_budget
    print(reqs)
    # Danh sách các bảng cần merge dựa trên yêu cầu
    tables_to_merge = []
    
    # Kiểm tra từng yêu cầu và thêm bảng tương ứng
    for req_key, (csv_file, rank_col) in requirements_map.items():
        if getattr(reqs, req_key):  # Kiểm tra nếu yêu cầu là True
            df = pd.read_csv(f"{device_path}/{csv_file}")
            df[rank_col] = df.index + 1
            tables_to_merge.append(df[["ids", "names", rank_col]])

    # Nếu không có yêu cầu nào, trả về phản hồi mặc định
    if not tables_to_merge:
        return f"Tôi đề xuất {device} từ {brand} dựa trên sở thích thương hiệu của bạn."

    # Merge các bảng
    combined_df = tables_to_merge[0]
    for df in tables_to_merge[1:]:
        combined_df = pd.merge(
            combined_df,
            df,
            on=["ids", "names"],
            how="outer"
        )

    # Điền NaN bằng rank tối đa + 1
    max_rank = max([len(df) for df in tables_to_merge]) + 1
    for col in combined_df.columns:
        if col.endswith("_rank"):
            combined_df[col] = combined_df[col].fillna(max_rank)

    # Tìm kiếm Elasticsearch nếu có yêu cầu cụ thể
    if reqs.specific_requirements and reqs.specific_requirements != '':
        print("reqs.specific_requirements",reqs.specific_requirements)
        search_results = search_elasticsearch(reqs.specific_requirements, ids=combined_df['ids'].to_list())
        print('search_results',search_results)
        # Tạo DataFrame từ kết quả Elasticsearch
        es_df = pd.DataFrame(search_results)
        es_df = es_df.rename(columns={"id": "ids", "product_name": "names"})
        
        # Nếu có kết quả từ Elasticsearch, merge với combined_df
        if not es_df.empty:
            combined_df = pd.merge(
                combined_df,
                es_df[["ids", "score"]],
                on="ids",
                how="left"
            )
            # Chuẩn hóa score (1 là tốt nhất, giá trị cao hơn là kém hơn)
            if "score" in combined_df:
                max_score = combined_df["score"].max()
                min_score = combined_df["score"].min()
                if max_score != min_score:
                    combined_df["es_rank"] = 1 + ((max_score - combined_df["score"]) / (max_score - min_score)) * (len(combined_df) - 1)
                else:
                    combined_df["es_rank"] = 1
                combined_df["es_rank"] = combined_df["es_rank"].fillna(len(combined_df) + 1)
        else:
            combined_df["es_rank"] = len(combined_df) + 1

    # Merge với bảng prices nếu có min_budget hoặc max_budget
    if min_budget or max_budget:
        prices_df = pd.read_csv(f"{device_path}/prices.csv")
        combined_df = pd.merge(
            combined_df,
            prices_df[["ids", "names", "current_prices"]],
            on=["ids", "names"],
            how="inner"  # Chỉ giữ các sản phẩm có giá
        )
        # Lọc theo khoảng giá
        if min_budget:
            combined_df = combined_df[combined_df["current_prices"] >= min_budget]
        if max_budget and max_budget!=0:
            combined_df = combined_df[combined_df["current_prices"] <= max_budget]

    # Tính combined_rank
    rank_columns = [col for col in combined_df.columns if col.endswith("_rank")]
    
    # Nếu có es_rank từ Elasticsearch, thêm vào rank_columns
    if "es_rank" in combined_df:
        rank_columns.append("es_rank")
    
    combined_df["combined_rank"] = combined_df[rank_columns].sum(axis=1)

    # Sắp xếp theo combined_rank từ thấp đến cao
    combined_df = combined_df.sort_values(by="combined_rank", ascending=True)

    # Lấy top k sản phẩm
    top_k_products = combined_df.head(k)
    
    # Xây dựng phản hồi
    if top_k_products.empty:
        return f"Không tìm thấy {device} phù hợp với yêu cầu của bạn."
    
    response = f"Dưới đây là top {k} {device} phù hợp với yêu cầu của bạn:\n"
    for i, product in top_k_products.iterrows():
        product_info = f"- {product['names']} (ID: {product['ids']}, combined rank: {int(product['combined_rank'])}"
        if "current_prices" in product:
            product_info += f", giá: {int(product['current_prices']):,} đồng"
        product_info += ")"
        
        # Thêm thông tin về các rank
        for req_key, (_, rank_col) in requirements_map.items():
            if getattr(reqs, req_key) and rank_col in product:
                product_info += f", {req_key} (rank {int(product[rank_col])})"
        
        # Thêm thông tin score từ Elasticsearch nếu có
        if "score" in product and not pd.isna(product["score"]):
            product_info += f", relevance score: {product['score']:.2f}"
        
        response += product_info + "\n"
    
    # Thêm thông tin về khoảng giá nếu có
    if min_budget and max_budget:
        response += f"(Trong khoảng giá {min_budget:,} - {max_budget:,} đồng)"
    elif min_budget:
        response += f"(Giá tối thiểu {min_budget:,} đồng)"
    elif max_budget:
        response += f"(Giá tối đa {max_budget:,} đồng)"
    
    return response
def get_close_name_tool(query: str):
    results = search_close_name(query)
    return f"Hỏi người dùng bạn sản phẩm mà họ đang muốn nhắc tới dựa vào các tên gần giống nhất sau:{', '.join([i.group_name for i in results])}"    

def product_information_tool(query: str)-> str:
    results = search_elasticsearch(query)
    print(results[0])
    return f"Đây là tài liệu về thông tin của sản phẩm mà người dùng đang nhắc tới: {results[0]['content']}"



def product_complain_tool(query: Complain) -> str:
    print(1)
    print(query)

    return "Vui lòng điền thông tin vào form, bạn sẽ được nhận được cuộc gọi tư vấn hỗ trợ trong vòng 48 giờ tiếp theo."

def shop_information_tool(query: str) -> str:
    with open('/home/kltn2025/spring_ecommerce/chatbot/src/rag/shop_document.txt') as f:
        shop_doc = f.read()

    return f"Dựa vào tài liệu sau về thông tin của cửa hàng để trả lời cho người dùng: {shop_doc}"

def get_related_product_tool(query: str) -> str:
    

    return f"Dựa vào tài liệu sau về thông tin của cửa hàng để trả lời cho người dùng: {shop_doc}"
