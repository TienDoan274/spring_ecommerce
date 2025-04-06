from pydantic import BaseModel
from typing import Optional

# Yêu cầu cho điện thoại
class PhoneRequirements(BaseModel):
    gaming: bool
    battery: bool
    camera: bool
    streaming: bool
    lightweight: bool
    min_budget: Optional[int]
    max_budget: Optional[int]
    brand_preference: Optional[str]
    specific_requirements: Optional[str]

# Yêu cầu cho laptop
class LaptopRequirements(BaseModel):
    ai_capable: bool
    gaming: bool
    office: bool
    graphics: bool
    engineering: bool
    lightweight: bool
    premium: bool
    min_budget: Optional[int]
    max_budget: Optional[int]
    brand_preference: Optional[str]
    specific_requirements: Optional[str]