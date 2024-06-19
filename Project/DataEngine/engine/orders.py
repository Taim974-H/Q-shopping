from dataclasses import asdict, dataclass, field
from datetime import date, datetime
from functools import partial
import random
from typing import List
from itertools import count


from faker import Faker
faker = Faker()


user_id_lower_bound = 1
user_id_upper_bound = 50

@dataclass
class Order:
    id: int = field(default_factory=count().__next__)
    timestamp: datetime = field(default_factory=datetime.now)
    statusDate: datetime = field(default_factory=datetime.now)
    addressLine: str = field(default_factory=faker.address)
    city: str = field(default_factory=faker.city)
    country: str = field(default_factory=faker.country)
    poBox: int = field(default_factory=partial(random.randint, 0, 9999))
    nameOnCard: str = field(default_factory=faker.name)
    expiryYear: int = field(default_factory=partial(
        random.randint,
        2022, 2030
    ))
    expiryMonth: int = field(default_factory=partial(
        random.randint,
        1, 12
    ))
    cvv: int = field(default_factory=partial(
        random.randint,
        100, 999
    ))
    cardNumber: int = field(default_factory=partial(
        random.randint,
        1000000000000000, 9999999999999999
    ))
    phoneNumber: int = field(default_factory=partial(
        random.randint,
        10000000, 99999999
    ))
    userId: int = field(default_factory=partial(
        random.randint,
        user_id_lower_bound, user_id_upper_bound
    ))
    status: str = field(default_factory=partial(
        random.choice,
        [
            "Shipping",
            "Processing",
            "Delivered"
        ]
    ))

    @property
    def __dict__(self):
        self.timestamp = "T".join(str(self.timestamp).split(" "))[:-3]
        self.statusDate = "T".join(str(self.statusDate).split(" "))[:-3]
        self.addressLine = " ".join(self.addressLine.split("\n"))
        return asdict(self)


def generate_orders(n: int) -> List[Order]:
    orders = [Order().__dict__ for _ in range(n)]
    return orders
