from abc import ABC,abstractmethod

class IPaymentStartegy(ABC):
    @abstractmethod
    def pay(self):
        pass



class CreditCardStategy(IPaymentStartegy):
    def pay(self):
        print("payment through crdticard")


class NetBankingStategy(IPaymentStartegy):
    def pay(self):
        print("payment through netbanking")


class UPIPayStategy(IPaymentStartegy):
    def pay(self):
        print("payment through payment strategy")

class Ipayments(ABC):
    @abstractmethod
    def pay(self):
        pass

class OnlinePayment(Ipayments):
    payment_strategy:IPaymentStartegy
    def __init__(self,payment_strategy:IPaymentStartegy):
        self.payment_strategy=payment_strategy

    def pay(self):
        self.payment_strategy.pay()

class CashOnDelivery(Ipayments):
    def pay(self):
        print("Payed IN COD")

class FlipKart:
    i_payment:Ipayments
    def __init__(self,ipay:Ipayments):
        self.i_payment=ipay

    def handle(self):
        self.i_payment.pay()


obj1=FlipKart(CashOnDelivery())
obj1.handle()

obj2=FlipKart(OnlinePayment(CreditCardStategy()))
obj2.handle()

