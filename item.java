public class Item {
    //instance variables
    private String name;
    private int cashAmount;
    private Room currentRoom;
    
    //initialize items
    public Item(String n, int cash, Room r){
        name = n;
        cashAmount = cash;
        currentRoom = r;
    }
    
    public String getItemName(){
        return name;
    }
    
    public int getCashAmount(){
        return cashAmount;
    }
    
    public Room getCurrentRoom(){ 
        return currentRoom;
    }
    public void setCurrentRoom(Room room)
    {
        currentRoom = room;
    }
    
}
