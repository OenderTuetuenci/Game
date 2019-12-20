package controller


import model._
import scalafx.scene.image.Image

class BoardController(gameController: GameController) {

    def newOwner(playerNr:Int,cell: Cell):Cell = {
        val updated = cell.asInstanceOf[Buyable].setOwner(playerNr)
        updated
    }

    def createBoard : Vector[Cell] = {
        var board: Vector[Cell] = Vector()
        board = board :+ CellFactory("Los", "Go", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/Go.png")) //todo go image
        board = board :+ CellFactory("Street", "Mediterranean Avenue", 1, 60, -1, 200, 0, mortgage = false, image = new Image("file:images/MediterraneanAve.png"))
        board = board :+ CellFactory("CommunityChest", "CommunityChest1", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/Go.png")) // todo
        board = board :+ CellFactory("Street", "Baltic Avenue", 1, 60, -1, 200, 0, mortgage = false, image = new Image("file:images/BalticAve.png"))
        board = board :+ CellFactory("IncomeTax", "IncomeTax", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve.png")) // todo
        board = board :+ CellFactory("Street", "Reading Railroad", 9, 200, -1, 200, 0, mortgage = false, image = new Image("file:images/ReadingRailroad.png"))
        board = board :+ CellFactory("Street", "Oriental Avenue", 2, 100, -1, 200, 0, mortgage = false, image = new Image("file:images/OrientalAve.png"))
        board = board :+ CellFactory("Eventcell", "Eventcell1", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve")) // todo
        board = board :+ CellFactory("Street", "Vermont Avenue", 2, 100, -1, 200, 0, mortgage = false, image = new Image("file:images/VermontAve.png"))
        board = board :+ CellFactory("Street", "Conneticut Avenue", 2, 120, -1, 200, 0, mortgage = false, image = new Image("file:images/ConneticutAve.png"))
        board = board :+ CellFactory("Jail", "Visit Jail or Jail", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve")) // todo

        board = board :+ CellFactory("Street", "St. Charles Place", 3, 140, -1, 200, 0, mortgage = false, image = new Image("file:images/StCharlesPlace.png"))
        board = board :+ CellFactory("Street", "Electric Company", 10, 150, -1, 200, 0, mortgage = false, image = new Image("file:images/ElectricCompany.png"))
        board = board :+ CellFactory("Street", "States Avenue", 3, 140, -1, 200, 0, mortgage = false, image = new Image("file:images/StatesAve.png"))
        board = board :+ CellFactory("Street", "Virgina Avenue", 3, 160, -1, 200, 0, mortgage = false, image = new Image("file:images/VirginiaAve.png"))
        board = board :+ CellFactory("Street", "Pennsylvania Railroad", 9, 200, -1, 200, 0, mortgage = false, image = new Image("file:images/PennsylvaniaRR.png"))
        board = board :+ CellFactory("Street", "St. James Place", 4, 180, -1, 200, 0, mortgage = false, image = new Image("file:images/StJamesPlace.png"))
        board = board :+ CellFactory("CommunityChest", "CommunityChest2", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve")) // todo
        board = board :+ CellFactory("Street", "Tennessee Avenue", 4, 180, -1, 200, 0, mortgage = false, image = new Image("file:images/TennesseeAve.png"))
        board = board :+ CellFactory("Street", "New York Avenue", 4, 200, -1, 200, 0, mortgage = false, image = new Image("file:images/NewYorkAve.png"))
        board = board :+ CellFactory("FreeParking", "Free parking", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve")) // todo

        board = board :+ CellFactory("Street", "Kentucky Avenue", 5, 220, -1, 200, 0, mortgage = false, image = new Image("file:images/KentuckyAve.png"))
        board = board :+ CellFactory("Eventcell", "Eventcell2", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve")) // todo
        board = board :+ CellFactory("Street", "Indiana Avenue", 5, 220, -1, 200, 0, mortgage = false, image = new Image("file:images/IndianaAve.png"))
        board = board :+ CellFactory("Street", "Illinois Avenue", 5, 240, -1, 200, 0, mortgage = false, image = new Image("file:images/IllinoisAve.png"))
        board = board :+ CellFactory("Street", "B & O Railroad", 9, 200, -1, 200, 0, mortgage = false, image = new Image("file:images/BnORailroad.png"))
        board = board :+ CellFactory("Street", "Atlantic Avenue", 6, 260, -1, 500, 0, mortgage = false, image = new Image("file:images/AtlanticAve.png"))
        board = board :+ CellFactory("Street", "Ventnor Avenue", 6, 260, -1, 800, 0, mortgage = false, image = new Image("file:images/VentnorAve.png"))
        board = board :+ CellFactory("Street", "Water Works", 10, 150, -1, 200, 0, mortgage = false, image = new Image("file:images/WaterWorks.png"))
        board = board :+ CellFactory("Street", "Marvin Gardens", 6, 280, -1, 2500, 0, mortgage = false, image = new Image("file:images/MarvinGardens.png"))
        board = board :+ CellFactory("GoToJail", "Go to jail", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve")) // todo

        board = board :+ CellFactory("Street", "Pacific Avenue", 7, 300, -1, 200, 0, mortgage = false, image = new Image("file:images/PacificAve.png"))
        board = board :+ CellFactory("Street", "North Carolina Avenue", 7, 300, -1, 200, 0, mortgage = false, image = new Image("file:images/NoCarolinaAve.png"))
        board = board :+ CellFactory("CommunityChest", "CommunityChest3", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve")) // todo
        board = board :+ CellFactory("Street", "Pennsylvania Avenue", 7, 320, -1, 200, 0, mortgage = false, image = new Image("file:images/PennsylvaniaAve.png"))
        board = board :+ CellFactory("Street", "Short Line Railroad", 9, 200, -1, 200, 0, mortgage = false, image = new Image("file:images/ShortLineRR.png"))
        board = board :+ CellFactory("Eventcell", "Eventcell3", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve")) // todo
        board = board :+ CellFactory("Street", "Park place", 8, 350, -1, 200, 0, mortgage = false, image = new Image("file:images/ParkPlace.png"))
        board = board :+ CellFactory("IncomeTax", "IncomeTax", 0, 0, 0, 0, 0, mortgage = false, image = new Image("file:images/OrientalAve"))
        board = board :+ CellFactory("Street", "Broadwalk", 8, 400, -1, 200, 0, mortgage = false, image = new Image("file:images/Broadwalk.png"))
        board
        // todo create xml or json from board
        // then add xml/json parser
    }

    object CellFactory {
        def apply(kind: String, name: String, group: Int, price: Int, owner: Int, rent: Int, home: Int, mortgage: Boolean,
                  image: Image): Cell = kind match {
            case "Los" => Los(name)
            case "Street" => Street(name, group, price, owner, rent, home, mortgage, image: Image)
            case "CommunityChest" => CommunityChest(name)
            case "IncomeTax" => IncomeTax(name)
            case "Eventcell" => Eventcell(name)
            case "Jail" => Jail(name)
            case "FreeParking" => FreiParken(name)
            case "GoToJail" => GoToJail(name)
            case "AdditionalTax" => Zusatzsteuer(name)
            case _ => throw new UnsupportedOperationException
        }
    }


    def activateStreet(field: Buyable): Unit = {
        if (field.owner == -1 && field.owner != gameController.isturn) gameController.notifyObservers(OpenBuyableFieldDialog(field))
        else gameController.notifyObservers(OpenPayRentDialog(field))
        //        val isturn = gameController.isturn
        //        val option = field.onPlayerEntered(isturn)
        //        val playerController = gameController.playerController
        //        gameController.printFun(optionEvent(option))
        //
        //        if (option == "buy") {
        //            // wer geld hat kauft die straße
        //            playerController.buy(field)
        //            //ansonsten miete zahlen falls keine hypothek
        //        } else if (option == "pay") {
        //            if (!field.mortgage) playerController.payRent(field)
        //            else gameController.printFun(streetOnHypothekEvent(field))
        //
        //        } else if (option == "buy home") {
        //            buyHome(field)
        //        }
    }

    def buyHome(field: Street): (Vector[Cell], Vector[Player]) = {
        val isturn = gameController.isturn
        var players = gameController.players
        var board = gameController.board
        if (players(isturn).money > 200 && !field.mortgage) // nur wenn straße nicht hypothek hat
            players = players.updated(isturn, players(isturn).decMoney(200))
        // todo if player owns group of streets buy house
        // todo if housecount = street.maxhouses buy hotel
        board = board.updated(players(isturn).position, board(players(isturn).position).asInstanceOf[Street].buyHome(1))
        (board, players)
    }

    def activateStart(field: Los): Unit = {
        field.onPlayerEntered(gameController.isturn)
        gameController.players = gameController.players.updated(gameController.isturn, gameController.players(gameController.isturn).incMoney(1000))
    }

    def activateJail(field: GoToJail): Unit = {
        field.onPlayerEntered(gameController.isturn)
        gameController.notifyObservers(openGoToJailDialog())
        gameController.players = gameController.players.updated(gameController.isturn, gameController.players(gameController.isturn).moveToJail)
        gameController.players = gameController.players.updated(gameController.isturn, gameController.players(gameController.isturn).incJailTime)
        gameController.notifyObservers(MovePlayerFigureEvent(gameController.players(gameController.isturn).figure, -350, 350)) // jailxy
        val rollDiceBUtton = gameController.currentStage.scene().lookup("#rollDice") //.asInstanceOf[javafx.scene.control.Button]
        rollDiceBUtton.setDisable(true)
        val endTurnButton = gameController.currentStage.scene().lookup("#endTurn") //.asInstanceOf[javafx.scene.control.Button]
        endTurnButton.setDisable(false)
    }

    /*
    def activateEvent(field: Eventcell): Unit = {
        field.onPlayerEntered(isturn)
    }



    def activateGoToJail(field: GoToJail): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateFreiParken(field: FreiParken): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateCommunityChest(field: CommunityChest): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateIncomeTax(field: IncomeTax): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateZusatzsteuer(field: Zusatzsteuer): Unit = {
        field.onPlayerEntered(isturn)
    }*/
}

