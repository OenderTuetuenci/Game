package controller


import model._

class BoardController() {

    def newOwner(playerNr:Int,cell: Cell):Cell = {
        val updated = cell.asInstanceOf[Buyable].setOwner(playerNr)
        updated
    }

    def createBoard : Vector[Cell] = {
        var board:Vector[Cell] = Vector()
        board = board :+ Cell("Los","Los",0,0,0,0,0,mortgage = false)
        board = board :+ Cell("Street","Street1",1,60,-1,200,0,mortgage = false)
        board = board :+ Cell("CommunityChest","CommunityChest1",0,0,0,0,0,mortgage = false)
        board = board :+ Cell("Street","Street2",1,60,-1,200,0,mortgage = false)
        board = board :+ Cell("IncomeTax","IncomeTax",0,0,0,0,0,mortgage=false)
        board = board :+ Cell("Trainstation","South Trainstation",9,200,-1,200,0,mortgage = false)
        board = board :+ Cell("Street","Street3",2,100,-1,200,0,mortgage = false)
        board = board :+ Cell("Eventcell","Eventcell1",0,0,0,0,0,mortgage = false)
        board = board :+ Cell("Street","Street4",2,100,-1,200,0,mortgage = false)
        board = board :+ Cell("Street","Street5",2,120,-1,200,0,mortgage = false)
        board = board :+ Cell("Jail","Visit Jail or Jail",0,0,0,0,0,mortgage = false)

        board = board :+ Cell("Street","Street6",3,140,-1,200,0,mortgage = false)
        board = board :+ Cell("ElectricityPlant","ElectricityPlant",10,150,-1,200,0,mortgage = false)
        board = board :+ Cell("Street","Street7",3,140,-1,200,0,mortgage = false)
        board = board :+ Cell("Street","Street8",3,160,-1,200,0,mortgage = false)
        board = board :+ Cell("Trainstation","West Trainstation",9,200,-1,200,0,mortgage = false)
        board = board :+ Cell("Street","Street9",4,180,-1,200,0,mortgage = false)
        board = board :+ Cell("CommunityChest","CommunityChest2",0,0,0,0,0,mortgage = false)
        board = board :+ Cell("Street","Street10",4,180,-1,200,0,mortgage = false)
        board = board :+ Cell("Street","Street11",4,200,-1,200,0,mortgage = false)
        board = board :+ Cell("FreeParking","Free parking",0,0,0,0,0,mortgage = false)

        board = board :+ Cell("Street","Street12",5,220,-1,200,0,mortgage = false)
        board = board :+ Cell("Eventcell","Eventcell2",0,0,0,0,0,mortgage = false)
        board = board :+ Cell("Street","Street13",5,220,-1,200,0,mortgage = false)
        board = board :+ Cell("Street","Street14",5,240,-1,200,0,mortgage = false)
        board = board :+ Cell("Trainstation","North Trainstation",9,200,-1,200,0,mortgage = false)
        board = board :+ Cell("Street","Street15",6,260,-1,500,0,mortgage = false)
        board = board :+ Cell("Street","Street16",6,260,-1,800,0,mortgage = false)
        board = board :+ Cell("Waterplant","Waterplant",10,150,-1,200,0,mortgage = false)
        board = board :+ Cell("Street","Street17",6,280,-1,2500,0,mortgage = false)
        board = board :+ Cell("GoToJail","Go to jail",0,0,0,0,0,mortgage = false)

        board = board :+ Cell("Street","Street18",7,300,-1,200,0,mortgage = false)
        board = board :+ Cell("Street","Street19",7,300,-1,200,0,mortgage = false)
        board = board :+ Cell("CommunityChest","CommunityChest3",0,0,0,0,0,mortgage = false)
        board = board :+ Cell("Street","Street20",7,320,-1,200,0,mortgage = false)
        board = board :+ Cell("Trainstation","East Trainstation",9,200,-1,200,0,mortgage = false)
        board = board :+ Cell("Eventcell","Eventcell3",0,0,0,0,0,mortgage = false)
        board = board :+ Cell("Street","Street21",8,350,-1,200,0,mortgage = false)
        board = board :+ Cell("IncomeTax","IncomeTax",0,0,0,0,0,mortgage = false)
        board = board :+ Cell("Street","Street22",8,400,-1,200,0,mortgage = false)
        board
    }
    object Cell {
        def apply(kind:String,name: String, group: Int, price: Int, owner: Int, rent: Int, home: Int, mortgage: Boolean): Cell = kind match {
            case "Los"=>Los(name)
            case "Street"=>Street(name,group,price,owner,rent,home,mortgage)
            case "CommunityChest"=>CommunityChest(name)
            case "IncomeTax"=>IncomeTax(name)
            case "Trainstation"=>Trainstation(name,group,price,owner,rent,mortgage)
            case "Eventcell"=>Eventcell(name)
            case "Jail"=>Jail(name)
            case "ElectricityPlant"=>Elektrizitaetswerk(name,group,price,owner,rent,mortgage)
            case "FreeParking"=>FreiParken(name)
            case "GoToJail"=>GoToJail(name)
            case "AdditionalTax"=>Zusatzsteuer(name)
            case "Waterplant"=>Wasserwerk(name,group,price,owner,rent,mortgage)
            case _=>throw new UnsupportedOperationException
        }
    }


    def activateStreet(field: Street): Unit = {
        val option = field.onPlayerEntered(isturn)
        notifyObservers(optionEvent(option))

        if (option == "buy") {
            // wer geld hat kauft die straße
            playerController.buyStreet(field)
            //ansonsten miete zahlen falls keine hypothek
        } else if (option == "pay") {
            if (!field.mortgage) playerController.payRent(field)
            else notifyObservers(streetOnHypothekEvent(field))

        } else if (option == "buy home") {
            buyHome(field)
        }
    }

    def buyHome(field: Street): Unit = {
        if (players(isturn).money > 200 && !field.mortgage) // nur wenn straße nicht hypothek hat
            players = players.updated(isturn, players(isturn).decMoney(200))
        // todo if player owns group of streets buy house
        // todo if housecount = street.maxhouses buy hotel
        board = board.updated(players(isturn).position, board(players(isturn).position).asInstanceOf[Street].buyHome(1))
    }

    def activateStart(field: Los): Unit = {
        field.onPlayerEntered(isturn)
        players = players.updated(isturn, players(isturn).incMoney(1000))
    }

    def activateEvent(field: Eventcell): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateJail(field: Jail): Unit = {
        field.onPlayerEntered(isturn)
        players = players.updated(isturn, players(isturn).moveToJail)
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

    def activateElektrizitaetswerk(field: Elektrizitaetswerk): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateTrainstation(field: Trainstation): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateWasserwerk(field: Wasserwerk): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateZusatzsteuer(field: Zusatzsteuer): Unit = {
        field.onPlayerEntered(isturn)
    }
}

