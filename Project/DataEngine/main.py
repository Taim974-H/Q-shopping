import json
from functools import partial

from engine.users import generate_users
from engine.orders import  generate_orders

file_func_map = {
    # "data/users.json": partial(generate_users, n = 50),
    "data/orders.json": partial(generate_orders, n = 50)
}

for path, method in file_func_map.items():
    with open(path, 'w', encoding='utf-8') as f:
        json.dump(method(), f)
