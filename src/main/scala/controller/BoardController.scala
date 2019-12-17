package controller


import model._

class BoardController(gameController: GameController) {

    def newOwner(playerNr:Int,cell: Cell):Cell = {
        val updated = cell.asInstanceOf[Buyable].setOwner(playerNr)
        updated
    }

    def createBoard : Vector[Cell] = {
        var board:Vector[Cell] = Vector()
        board = board :+ CellFactory("Los","Los",0,0,0,0,0,mortgage = false)
        board = board :+ CellFactory("Street","Street1",1,60,-1,200,0,mortgage = false)
        board = board :+ CellFactory("CommunityChest","CommunityChest1",0,0,0,0,0,mortgage = false)
        board = board :+ CellFactory("Street","Street2",1,60,-1,200,0,mortgage = false)
        board = board :+ CellFactory("IncomeTax", "IncomeTax", 0, 0, 0, 0, 0, mortgage = false)
        board = board :+ CellFactory("Street", "South Trainstation", 9, 200, -1, 200, 0, mortgage = false)
        board = board :+ CellFactory("Street", "Street3", 2, 100, -1, 200, 0, mortgage = false)
        board = board :+ CellFactory("Eventcell","Eventcell1",0,0,0,0,0,mortgage = false)
        board = board :+ CellFactory("Street","Street4",2,100,-1,200,0,mortgage = false)
        board = board :+ CellFactory("Street","Street5",2,120,-1,200,0,mortgage = false)
        board = board :+ CellFactory("Jail","Visit Jail or Jail",0,0,0,0,0,mortgage = false)

        board = board :+ CellFactory("Street", "Street6", 3, 140, -1, 200, 0, mortgage = false)
        board = board :+ CellFactory("Street", "ElectricityPlant", 10, 150, -1, 200, 0, mortgage = false)
        board = board :+ CellFactory("Street", "Street7", 3, 140, -1, 200, 0, mortgage = false)
        board = board :+ CellFactory("Street", "Street8", 3, 160, -1, 200, 0, mortgage = false)
        board = board :+ CellFactory("Street", "West Trainstation", 9, 200, -1, 200, 0, mortgage = false)
        board = board :+ CellFactory("Street", "Street9", 4, 180, -1, 200, 0, mortgage = false)
        board = board :+ CellFactory("CommunityChest","CommunityChest2",0,0,0,0,0,mortgage = false)
        board = board :+ CellFactory("Street","Street10",4,180,-1,200,0,mortgage = false)
        board = board :+ CellFactory("Street","Street11",4,200,-1,200,0,mortgage = false)
        board = board :+ CellFactory("FreeParking","Free parking",0,0,0,0,0,mortgage = false)

        board = board :+ CellFactory("Street","Street12",5,220,-1,200,0,mortgage = false)
        board = board :+ CellFactory("Eventcell","Eventcell2",0,0,0,0,0,mortgage = false)
        board = board :+ CellFactory("Street","Street13",5,220,-1,200,0,mortgage = false)
        board = board :+ CellFactory("Street", "Street14", 5, 240, -1, 200, 0, mortgage = false)
        board = board :+ CellFactory("Street", "North Trainstation", 9, 200, -1, 200, 0, mortgage = false)
        board = board :+ CellFactory("Street", "Street15", 6, 260, -1, 500, 0, mortgage = false)
        board = board :+ CellFactory("Street", "Street16", 6, 260, -1, 800, 0, mortgage = false)
        board = board :+ CellFactory("Street", "Waterplant", 10, 150, -1, 200, 0, mortgage = false)
        board = board :+ CellFactory("Street", "Street17", 6, 280, -1, 2500, 0, mortgage = false)
        board = board :+ CellFactory("GoToJail","Go to jail",0,0,0,0,0,mortgage = false)

        board = board :+ CellFactory("Street","Street18",7,300,-1,200,0,mortgage = false)
        board = board :+ CellFactory("Street","Street19",7,300,-1,200,0,mortgage = false)
        board = board :+ CellFactory("CommunityChest","CommunityChest3",0,0,0,0,0,mortgage = false)
        board = board :+ CellFactory("Street", "Street20", 7, 320, -1, 200, 0, mortgage = false)
        board = board :+ CellFactory("Street", "East Trainstation", 9, 200, -1, 200, 0, mortgage = false)
        board = board :+ CellFactory("Eventcell", "Eventcell3", 0, 0, 0, 0, 0, mortgage = false)
        board = board :+ CellFactory("Street","Street21",8,350,-1,200,0,mortgage = false)
        board = board :+ CellFactory("IncomeTax","IncomeTax",0,0,0,0,0,mortgage = false)
        board = board :+ CellFactory("Street","Street22",8,400,-1,200,0,mortgage = false)
        board
    }
    object CellFactory {
        def apply(kind:String,name: String, group: Int, price: Int, owner: Int, rent: Int, home: Int, mortgage: Boolean): Cell = kind match {
            case "Los"=>Los(name)
            case "Street"=>Street(name,group,price,owner,rent,home,mortgage)
            case "CommunityChest"=>CommunityChest(name)
            case "IncomeTax"=>IncomeTax(name)
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

