from typing_extensions import TypedDict, Any,List

class Complain(TypedDict):
    product_names: List[str]
    problems: List[str]