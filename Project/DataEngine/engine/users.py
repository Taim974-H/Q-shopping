from dataclasses import asdict, dataclass, field
from typing import List
from itertools import count
from faker import Faker
faker = Faker()

@dataclass
class User:
    id: int = field(default_factory=count().__next__)
    fullName: str = field(default_factory=faker.name)
    userName: str = field(default_factory=faker.unique.first_name)
    email: str = field(default_factory=faker.unique.email)
    password: str = field(default="pass")
    isAdmin: bool = False
    
    @property
    def __dict__(self):
        return asdict(self)
    

def generate_users(n: int) -> List[User]:
    users =  [User().__dict__ for _ in range(n)]
    users.pop(0)
    users.extend([
        User(
            fullName="ahmad",
            userName="ahmadlol",
            email="ahmadlol@gamil.com",
            password="123",
        ).__dict__,
        User(
            fullName="admin",
            userName="admin",
            email="admin@admin.com",
            password="admin",
            isAdmin=True
        ).__dict__
        
    ])
    return users