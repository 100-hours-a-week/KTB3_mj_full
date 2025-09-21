package cafe;

import java.util.*;

class MenuStore{
    Map<Integer,Category> categories = new LinkedHashMap<>();
    Map<Integer,Menu> menuIndex = new HashMap<>();

    static MenuStore defaultStore(){
        MenuStore store = new MenuStore();

        List<Menu> coffees = Arrays.asList(// 커피 메뉴 추가
                new Menu(101, "아이스 아메리카노", 3000),
                new Menu(102, "라떼", 3500),
                new Menu(103, "핫초코", 4000),
                new Menu(104, "바닐라 라떼", 3800),
                new Menu(105, "카라멜 마끼아또", 4500),
                new Menu(106, "에스프레소", 2500),
                new Menu(107, "콜드브루", 4200)
        );

        List<Menu> breads = Arrays.asList(//빵 메뉴 추가
                new Menu(201, "식빵", 3000),
                new Menu(202, "머핀", 3500),
                new Menu(203, "케이크", 4500),
                new Menu(204, "크루아상", 3200),
                new Menu(205, "치즈 베이글", 3300),
                new Menu(206, "소시지롤", 3800)
        );
        store.categories.put(1, new Category("커피", coffees));
        store.categories.put(2, new Category("빵", breads));
        return store;
    }

    List<Menu> allMenus() {
        List<Menu> all = new ArrayList<>();
        for (Category c : categories.values()) all.addAll(c.items);
        return all;
    }
}