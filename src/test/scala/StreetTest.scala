//import model.Street
//import org.scalatest.{Matchers, WordSpec}
//
//class StreetTest extends WordSpec with Matchers {
//  "A Street" when { "new" should{
//    val street = Street("mystreet", 1, 3000, -1, 1, 0, mortgage = false) // NO NULL ( option )
//    "have a name length " in {
//      street.name.length should be > 0
//      street.name.length should be < 20
//    }
//    "should have cool String representation" in{
//      street.toString should be ("mystreet group: 1 price: 3000 rent: 1 homecount: 0 mortgage: false")
//    }
//    "should have a gruop" in{
//      street.group should be (1)
//    }
//    "should have a price" in{
//      street.price should be(3000)
//    }
//    "should have no owner" in {
//      street.owner should be(-1)
//    }
//    "should have a rent" in{
//      street.rent should be(1)
//    }
//    "should have a homecount of 0" in{
//      street.home should be(0)
//    }
//    "should have no mortgage" in{
//      street.mortgage should be(false)
//    }
//  }
//    "entered by a player return  buy,buy home or pay" in{
//      val street = Street("mystreet", 1, 3000, -1, 1, 0, mortgage = false)
//      val bought = street.setOwner(1)
//      street.onPlayerEntered(1) should be("buy")
//      bought.onPlayerEntered(1) should be("buy home")
//      bought.onPlayerEntered(2) should be("pay")
//    }
//  }
//  "can switch owner" in{
//    val street = Street("mystreet", 1, 3000, -1, 1, 0, mortgage = false)
//    val owner = street.setOwner(5)
//    owner.owner should be(5)
//  }
//  "return its mortgage status" in{
//    val street = Street("mystreet", 1, 3000, -1, 1, 0, mortgage = false)
//    val mortgage2 = street.payMortgage
//    val mortgage = street.getMortgage
//    mortgage2.mortgage should be(false)
//    mortgage.mortgage should be(true)
//  }
//  "can buy/sell homes" in{
//    val street = Street("mystreet", 1, 3000, -1, 1, 0, mortgage = false)
//    val home = street.buyHome(1)
//    val sell = home.sellHome(1)
//    home.home should be(1)
//    sell.home should be(0)
//  }
//}
