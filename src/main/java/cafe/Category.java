package cafe;


import java.util.List;

class Category{
    String name;
    List<Menu> items;
    Category(String name, List<Menu> items){
        this.name = name;
        this.items = items;
    }
}