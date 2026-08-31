from typing import List
from abc import ABC, abstractmethod

class Product:
    def __init__(self, name: str, price: float):
        self.name = name
        self.price = price

class Shelf:
    def __init__(self, code: str, products: List[Product]):
        self.stock = len(products)
        self.products = products
        self.code = code

    def has_stock(self) -> bool:
        return self.stock > 0

    def decrease(self):
        if self.has_stock():
            self.stock -= 1
            return self.products.pop(0) # Actually remove the product
        raise Exception("NO STOCK AVAILABLE")

# --- Coin Storage ---
class CoinVault:
    def __init__(self):
        self.total_balance = 0.0

    def add_coins(self, amount: float):
        self.total_balance += amount

# --- Payment Strategy ---
class Payment(ABC):
    @abstractmethod
    def handle_payment(self, amount_inserted: float, price: float) -> float:
        pass

class CoinPayment(Payment):
    def handle_payment(self, amount_inserted: float, price: float) -> float:
        if amount_inserted < price:
            raise Exception(f"Not enough money. Inserted: ${amount_inserted:.2f}, Price: ${price:.2f}")
        # Return change (amount inserted minus price)
        return amount_inserted - price

    # --- State Pattern ---
class VendingMachineState(ABC):
    def __init__(self, machine):
        self.machine = machine

    @abstractmethod
    def select_product(self, code: str): pass

    @abstractmethod
    def insert_money(self, amount: float): pass

class IdleState(VendingMachineState):
    def select_product(self, code: str):
        for shelf in self.machine.shelves:
            if code == shelf.code:
                if shelf.has_stock():
                    self.machine.selected_shelf = shelf
                    print(f"Selected {shelf.products[0].name}. Please insert ${shelf.products[0].price:.2f}")
                    # Transition to next state
                    self.machine.set_state(self.machine.has_selection_state)
                    return
                else:
                    print(f"Item {code} is OUT OF STOCK.")
                    return
        print("Invalid code.")

    def insert_money(self, amount: float):
        print("Please select a product first.")

class HasSelectionState(VendingMachineState):
    def select_product(self, code: str):
        print("Product already selected. Please insert money or cancel.")

    def insert_money(self, amount: float):
        self.machine.current_transaction_amount += amount
        price = self.machine.selected_shelf.products[0].price

        try:
            # Process payment
            change = self.machine.payment_processor.handle_payment(
                self.machine.current_transaction_amount,
                price
            )

            # Add to machine's overall vault
            self.machine.vault.add_coins(price)

            # Dispense
            product = self.machine.selected_shelf.decrease()
            print(f"Dispensed {product.name}. Change returned: ${change:.2f}")

            # Reset transaction and state
            self.machine.current_transaction_amount = 0
            self.machine.selected_shelf = None
            self.machine.set_state(self.machine.idle_state)

        except Exception as e:
            print(e) # Catch "Not enough money" exception and wait for more coins

# --- Context (The Machine itself) ---
class VendingMachine:
    def __init__(self, shelves: List[Shelf], payment_processor: Payment):
        self.shelves = shelves
        self.payment_processor = payment_processor
        self.vault = CoinVault()

        self.selected_shelf = None
        self.current_transaction_amount = 0.0

        # Initialize States
        self.idle_state = IdleState(self)
        self.has_selection_state = HasSelectionState(self)

        # Set initial state
        self.current_state = self.idle_state

    def set_state(self, state: VendingMachineState):
        self.current_state = state

    def select_product(self, code: str):
        self.current_state.select_product(code)

    def insert_money(self, amount: float):
        self.current_state.insert_money(amount)

    def show_products(self):
        print("\n--- Vending Machine Inventory ---")
        for shelf in self.shelves:
            if shelf.has_stock():
                prod = shelf.products[0]
                print(f"[{shelf.code}] {prod.name} - ${prod.price:.2f} ({shelf.stock} in stock)")
            else:
                print(f"[{shelf.code}] OUT OF STOCK")
        print("---------------------------------")


# --- User Interaction ---
class User:
    def __init__(self, name: str, machine: VendingMachine):
        self.name = name
        self.machine = machine

    def view_products(self):
        self.machine.show_products()

    def purchase(self, code: str, coins: float):
        print(f"\n[{self.name}] attempts to buy '{code}' and inserts ${coins:.2f}.")
        self.machine.select_product(code)

        # Only insert money if the machine accepted the code (moved to HasSelectionState)
        if isinstance(self.machine.current_state, HasSelectionState):
            self.machine.insert_money(coins)


# --- Main Execution ---
class Main:
    @staticmethod
    def run():
        # 1. Create inventory
        coke_products = [Product("Coke", 1.50) for _ in range(2)]
        water_products = [Product("Water", 1.00) for _ in range(1)]

        shelves = [
            Shelf("A1", coke_products),
            Shelf("A2", water_products)
        ]

        # 2. Setup Vending Machine
        machine = VendingMachine(shelves, CoinPayment())

        # 3. Create a User
        bob = User("Bob", machine)

        # 4. Run through scenarios
        bob.view_products()

        # Scenario 1: Successful purchase with exact change
        bob.purchase("A2", 1.00)

        # Scenario 2: Successful purchase requiring change
        bob.purchase("A1", 2.00)

        # Scenario 3: Not enough money inserted initially
        bob.purchase("A1", 1.00)
        print("\n[Bob] realizes he needs more money and inserts $0.50.")
        machine.insert_money(0.50) # Completes the transaction

        # Scenario 4: Out of stock
        bob.purchase("A2", 1.00)

if __name__ == "__main__":
    Main.run()