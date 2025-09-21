package cafe;

class Menu{
    int id;
    String name;
    int price;
    int cnt;//개수

    Menu(int id,String name, int price){
        this.id = id;
        this.name = name;
        this.price = price;
        this.cnt = 0;
    }

    void add(int qty){
        if(qty > 0){
            this.cnt = this.cnt + qty;
        }
    }

    int subtotal(){
        return this.cnt * this.price;
    }

    String label(){
        return this.name + " " + price + "원 (현재 "+ this.cnt +"개)";
    }

}