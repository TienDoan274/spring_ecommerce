from llama_index.core.agent import ReActAgent
from llama_index.core.tools import FunctionTool
from llama_index.core.llms import ChatMessage
from typing import List, Dict, Any
from dotenv import load_dotenv
import os
from typing_extensions import TypedDict
from models import Requirements, ComparisionInput, ProductInform, Complain
from rag.retrieve import combine_results
from llama_index.llms.google_genai import GoogleGenAI
from .prompts import *
from .tools import *
load_dotenv()

def create_llama_tools():
    tools = [
        FunctionTool.from_defaults(
            fn=product_consultation_tool,
            name="product_consultation_tool",
            description="Use this tool when users need help finding a suitable electronic device. Requires the device type (e.g., 'phone', 'laptop') and the original query."
        ),
        FunctionTool.from_defaults(
            fn=product_information_tool,
            name="product_information_tool",
            description="Use this tool to retrieve product information when users request specific details about a product or compare different products. Requires the specific product name."
        ),
        
        FunctionTool.from_defaults(
            fn=product_complain_tool,
            name="product_complain_tool",
            description="Use this tool when users raise complaints about products or services. Requires the original query to process the complaint."
        ),
        FunctionTool.from_defaults(
            fn=shop_information_tool,
            name="shop_information_tool",
            description="Use this tool when users ask about shop details such as addresses, operating hours, hotlines, promotions, warranty periods, or return policies. Requires the original query; device type is optional."
        ),
        FunctionTool.from_defaults(
            fn=get_close_name_tool,
            name="get_close_name_tool",
            description="Use this tool to identify the exact product name when the user's query is vague. Returns a list of similar product names to clarify with the user."
        )
    ]
    return tools

# Tạo LlamaIndex agent
def create_manager_agent():
    llm = GoogleGenAI(
        model="gemini-2.0-flash",
    )
    tools = create_llama_tools()
    
    # Load prompt từ file nếu có
    manager_instructions = MANAGER_INSTRUCTION
    
    agent = ReActAgent.from_tools(
        tools,
        llm=llm,
        verbose=True,
        system_prompt=manager_instructions
    )
    
    return agent

# Xử lý chat
async def process_chat(query: str, chat_history: List[Dict] = None):
    if chat_history is None:
        chat_history = []
    
    # Chuyển đổi lịch sử chat sang định dạng của LlamaIndex
    llama_messages = []
    for message in chat_history:
        if message["role"] == "user":
            llama_messages.append(ChatMessage(role="user", content=message["content"]))
        elif message["role"] == "assistant":
            llama_messages.append(ChatMessage(role="assistant", content=message["content"]))
    
    # Tạo agent và xử lý
    agent = create_manager_agent()
    
    # Separate the query from chat history
    if llama_messages:
        # If there's chat history, pass it as a separate parameter
        response = await agent.achat(query, chat_history=llama_messages)    
    else:
        # If there's no chat history, just pass the query
        response = await agent.achat(query)
    
    return response.response