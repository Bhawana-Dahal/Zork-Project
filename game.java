import java.util.*;
/**
 * Class Game - the main class of the "Zork" game.
 *
 * Author:  Michael Kolling
 * Version: 1.1
 * Date:    March 2000
 * 
 * Modified: Kevin Good
 * Date:     Feb 2024
 * 
 *  This class is the main class of the "Zork" application. Zork is a very
 *  simple, text based adventure game.  Users can walk around some scenery.
 *  That's all. It should really be extended to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  routine.
 * 
 *  This main class creates and initializes all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates the
 *  commands that the parser returns.
 */

class Game 
{
    private Parser parser;
    private Room currentRoom;
    private  ArrayList<Room> roomHistory = new ArrayList<Room>();
    private  ArrayList<Item> inventory = new ArrayList<Item>();
    private  ArrayList<Item> gameItems = new ArrayList<Item>();

    /**
     * Create the game and initialize its internal map.
     */
    public Game() 
    {
        createStuff();
        parser = new Parser();
    }

    

      //Create all the rooms and link their exits together.
     
    private void createStuff()
    {
        Room getawayCar, bankTellers, storage, office1, breakroom, office2, files, vault;
        Item goldBars, cashBundles, keyCard, painting, bankTeller, lunch, atm, cash;
        // create the rooms
        getawayCar = new Room("the getaway car, infront of the bank doors sits a ATM. ", "GATAWAYCAR");
        bankTellers = new Room("bank tellers area, the teller's drawers are full of cash.", "BANKTELLERS");
        storage = new Room("the storage space, here lies bars of gold.", "STORAGE");
        office1 = new Room("the first office room, looks likes someone left their key card on their desk.", "OFFICE1");
        breakroom = new Room("the employees breakroom, the teller left their yummy lunch unattended.", "BREAKROOM");
        office2 = new Room("the second office room, a beautiful painting sits on the wall... looks expensive. ", "OFFICE2");
        files = new Room("the files/documents room, a giant metal door is to your west.", "FILES");
        vault = new Room("the Vault! You've never seen this much CASH in your life.", "VAULT");
        
        
        
        // initialise room exits
        getawayCar.setExits(null, null, bankTellers, null);
        files.setExits(null, breakroom, null, vault);
        bankTellers.setExits(getawayCar, office1, breakroom, storage);
        office1.setExits(null, null, null, bankTellers);
        breakroom.setExits(bankTellers, office2, null, files);
        office2.setExits(null, null, null, breakroom);
        storage.setExits(null, bankTellers, null, null);
        vault.setExits(null, files, null, null);

        currentRoom = getawayCar;  
        // start game in getaway car
        
        //create the items
        goldBars = new Item("gold", 10000, storage);
        cashBundles = new Item("cash-bundle", 5000, bankTellers);
        keyCard = new Item("keyCard", 0, office1);
        painting = new Item("painting", 7500, office2);
        bankTeller = new Item("bankTeller", 0, bankTellers);
        lunch = new Item("lunch", 5, breakroom);
        atm = new Item("ATM", 2500, getawayCar);
        cash = new Item("cash", 20000, vault);
        gameItems.add(goldBars);
        gameItems.add(cashBundles);
        gameItems.add(keyCard);
        gameItems.add(painting);
        gameItems.add(bankTeller);
        gameItems.add(lunch);
        gameItems.add(atm);
        gameItems.add(cash);
    }
/**
    private void createItems()
    {
        Item goldBars, cashBundles, keyCard, painting, bankTeller, lunch, atm;
        //create the items
        goldBars = new Item("gold", 10000, storage);
        cashBundles = new Item("cash", 5000, bankTellers);
        keyCard = new Item("key card", 0, office1);
        painting = new Item("painting", 7500, office2);
        bankTeller = new Item("the bank teller", 0, bankTellers);
        lunch = new Item("bank teller's lunch", 5, breakroom);
        atm = new Item("ATM", 2500, getawayCar);
    }


    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
        long startTime = System.nanoTime();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        while (! finished)
        {
             Command command = parser.getCommand();
            finished = processCommand(command);
            
            long elapsed = (System.nanoTime()-startTime); //this checks the time and putting it inside a while loop allows it to constantly check if time is over 2 minutes
            //System.out.println(elapsed);
            if(elapsed > 120000000000L)  //if over 2 mins
        { 
            System.out.println("\u001B[31mTimes up!\u001B[0m");
            
             int total = 0;
            for(Item item: inventory)
            {
            total += item.getCashAmount();
            }
            if(total >=20000) //did you get enough money to win your family back?
            {
                System.out.println("you stole enough money to save your family! 😁");
                finished = true;
            }
            else
            {
                System.out.println("You didn't get enough money...");
                finished = true;
            }
            
            
        }
        }

    }
    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Your familys been abducted and you need to rob this bank to pay the ransom fee!");
        System.out.println("You have two minutes to grab as much money as you can!");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.longDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) 
    {
        if(command.isUnknown())
        {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord(); //all the things the player can do/say
         ArrayList<Item> things = new ArrayList<Item>();
        if (commandWord.equals("help"))
            printHelp();
        else if (commandWord.equals("go"))
            goRoom(command);
            else if (commandWord.equals("dance"))
            goDance();
            else if (commandWord.equals("pray"))
            goPray();
            //.getCurrentRoom() //update current room every move
        else if (commandWord.equals("take"))
            take(command);
            else if (commandWord.equals("drop"))
            drop(command);
             else if (commandWord.equals("show"))
            show();
        else if (commandWord.equals("back"))
            goBack();  //go back one after tracking every current room
        else if (commandWord.equals("quit"))
        {
            if(command.hasSecondWord())
                System.out.println("Quit what?");
            else
                return true;  // signal that we want to quit
        }
        return false;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() //playet can get help
    {
        System.out.println("You are poor. You are desperate. You must rob the bank or your family will DIE!");
        System.out.println("Navigate the bank and exit with the most money possible");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    private void take(Command command)
    {
        if(!command.hasSecondWord())
        {
            // if there is no second word, we don't know where to go...
            System.out.println("Take what?");
            return;
        }
        if(inventory.size()>=2)
        {
            System.out.println("Your inventory is full"); //can only pick up 2 items
            return;
        }
        else
        {
            String item = command.getSecondWord();
            for(int i  = 0; i<gameItems.size(); i++)
            {
                if(gameItems.get(i).getItemName().equals(item))  //if item is valid
                {
                    if(gameItems.get(i).getCurrentRoom() == currentRoom) //if the item is in the room with us
                    {
                    inventory.add(gameItems.get(i)); //added to inventory
                    System.out.println("You sucessfully picked up the " + item);
                    }
                    else
                    {
                        System.out.println("you didnt grab anything!"); //not in the room with us/doesnt exist
                    }
                }
                
            }
            
        }
        
    }
     private void goDance()
    {
         System.out.println("you bust a move!!!");
    }
    
    private void drop(Command command)
    {
        if(!command.hasSecondWord())
        {
            // if there is no second word, we don't know where to go...
            System.out.println("drop what?");  //user only said drop
            return;
        }
         String thing = command.getSecondWord();
            for(int i  = 0; i<gameItems.size(); i++)
            {
                if(gameItems.get(i).getItemName().equals(thing))  //if dropped item is vaild
                {
                    inventory.remove(gameItems.get(i));
                 System.out.println("You sucessfully dropped " + thing);  //removed from inventory
                 Item it = gameItems.get(i);
                 it.setCurrentRoom(currentRoom);  //item's new location is current room so it can be picked up again in the room it was dropped in
                }
                
            }
    }
    

    private void show()
    {
        System.out.println("You're holding: ");
        for(Item item: inventory)
        System.out.println (item.getItemName()); // shows you what's in your inventory
    }
    private void goPray() 
    {
        System.out.println("you pray you and your family survives...");
    }
    
    
    private void goBack()
    {
        if(roomHistory.size() == 0)  //cant go back if the arraylist is empty
        {
            System.out.println("You haven't gone anywhere yet");
        }
    
    else
    {   
        roomHistory.add(currentRoom); //adds to the arrayList keeping track of all your movements
        currentRoom = roomHistory.get(roomHistory.size()-2);
        System.out.println(currentRoom.longDescription());
    }
    }
   
    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        
        if(!command.hasSecondWord())
        {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.nextRoom(direction);
        //System.out.println(System.nanoTime()/(1000000000));
       if (nextRoom == null) 
       {
        System.out.println("There is no door that direction!");   
       }
     
       else {
           roomHistory.add(currentRoom);
           currentRoom = nextRoom;
           boolean hasKeyCard = false;  //do you have a keycard?
           for(Item item: inventory) 
           {
               hasKeyCard = item.getItemName().contains("keyCard");
           }
           if(currentRoom.getRoomCode().equals("VAULT") && hasKeyCard)  //can enter with keycard
               {
                   System.out.println("you've got the keycard, you can go to the vault!");
                   System.out.println(currentRoom.longDescription());
               }
               else if(currentRoom.getRoomCode().equals("VAULT") && !hasKeyCard) //you enter the vault, goBack() is implimented, and then you get told you need a keycard
               {
                   System.out.println("you cant go that way.. you're missing or already used a keycard..");
                   goBack();
                   
               }
               else
               {
                   System.out.println(currentRoom.longDescription()); //description of the room you're in
               }
           
            }
        
}
}
