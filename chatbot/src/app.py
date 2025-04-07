from flask import Flask, request, jsonify
from concurrent.futures import ThreadPoolExecutor
from agents.llama_agent import process_chat
from filter.preprocess import Filter
from dotenv import load_dotenv
import os

load_dotenv()
app = Flask(__name__)
executor = ThreadPoolExecutor()

conversation_history = {}

@app.route("/chat", methods=["POST"])
def chat():
    data = request.json
    query = data.get("message", "")
    thread_id = data.get("thread_id", "1")

    # Kiểm tra filter
    filter_result = Filter.filter_query(query=query)
    if filter_result == 0:
        return jsonify({
            "role": "assistant",
            "content": "Trong input có chứa từ nhạy cảm"
        })
    elif filter_result == 1:
        return jsonify({
            "role": "assistant",
            "content": "Chỉ hỗ trợ ngôn ngữ Tiếng Việt và Tiếng Anh"
        })
    elif filter_result == 2:
        return jsonify({
            "role": "assistant",
            "content": "Xin chào tôi là NextUS, trợ lý ảo thông minh với các tính năng ..."
        })
    
    if not query:
        return jsonify({"error": "Missing query parameter"}), 400

    # Lấy lịch sử cuộc trò chuyện hoặc tạo mới
    if thread_id not in conversation_history:
        conversation_history[thread_id] = []
    
    # Xử lý chat sử dụng LlamaIndex agent trong thread riêng
    future = executor.submit(process_chat_sync, query, conversation_history[thread_id])
    response = future.result()
    
    # Cập nhật lịch sử cuộc trò chuyện
    conversation_history[thread_id].append({"role": "user", "content": query})
    conversation_history[thread_id].append({"role": "assistant", "content": str(response)})
    
    return jsonify({
        "role": "assistant",
        "content": str(response)
    })

def process_chat_sync(query, history):
    """Synchronous wrapper for the async process_chat function"""
    import asyncio
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    result = loop.run_until_complete(process_chat(query, history))
    loop.close()
    return result

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001, debug=True)