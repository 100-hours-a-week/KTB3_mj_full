package cafe;

import java.util.*;

public class Cafe {

    static class CountdownThread extends Thread{
        final int seconds;
        CountdownThread(int seconds){
            super("CountdownThread");
            this.seconds = seconds;
            setDaemon(true);
        }
        @Override
        public void run() {
            for(int t = seconds; t >= 1; t--){
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    return;
                }
                System.out.println("[안내] 입력 대기 종료까지 " + t + "초");
            }
            System.out.println("[안내] 입력 시간이 초과되어 프로그램을 종료합니다. ");
            System.exit(0);
        }
    }

    static int readIntWithTimer(Scanner sc,String prompt, int seconds){
        System.out.print(prompt);

        CountdownThread countdownThread = new CountdownThread(seconds);
        countdownThread.start();
        while(true){
            if(sc.hasNextInt()){
                int v = sc.nextInt();
                countdownThread.interrupt();
                return v;
            } else {
                System.out.println("[안내] 숫자를 입력해주세요: ");
                sc.next();
            }
        }
    }

    static int handleCategory(Scanner sc, Category category, int pageSize){
        int addedTotal = 0;
        int totalItems = category.items.size();
        int totalPages = Math.max(1, (int) Math.ceil(totalItems / (double) pageSize));
        int page = 0; // 0-based

        while(true){
            int start = page * pageSize;
            int end = Math.min(start + pageSize, totalItems);
            List<Menu> pageItems = category.items.subList(start, end);

            System.out.println("\n----- " + category.name + " (페이지 " + (page+1) + "/" + totalPages + ") -----");

            for(int i = 0; i < pageItems.size(); i++){
                System.out.println((i+1) + ". " + pageItems.get(i).label());
            }

            // 동적 옵션 번호
            int opt = pageItems.size();
            int prevNo = -1, nextNo = -1;

            if (page > 0) {
                prevNo = ++opt;
                System.out.println(prevNo + ". 이전 페이지");
            }
            if (page < totalPages - 1) {
                nextNo = ++opt;
                System.out.println(nextNo + ". 다음 페이지");
            }

            int backNo = ++opt;
            System.out.println(backNo + ". 뒤로가기");

            int sel = readIntWithTimer(sc, "선택: ", 10);

            // 뒤로가기
            if (sel == backNo) break;

            // 이전/다음 페이지
            if (sel == prevNo) { page--; continue; }
            if (sel == nextNo) { page++; continue; }

            // 메뉴 선택
            if (1 <= sel && sel <= pageItems.size()) {
                Menu m = pageItems.get(sel - 1);
                int qty = readIntWithTimer(sc, "개수: ", 10);
                if(qty <= 0){
                    System.out.println("[안내] 1개 이상 입력하세요.");
                    continue;
                }
                m.add(qty);
                int inc = m.price * qty;
                addedTotal += inc;
                System.out.println("[담기 완료] " + m.name + " " + qty + "개 → +" + inc + "원");
            } else {
                System.out.println("[안내] 올바른 번호를 선택해주세요.");
            }
        }
        return addedTotal;
    }

    static void printReceipt(List<Menu> allMenus){
        System.out.println("\n------영수증------");
        int total = 0;
        for(Menu m : allMenus){
            if(m.cnt > 0){
                int sub = m.subtotal();
                total += sub;
                System.out.println(m.name + " x " + m.cnt + " = " + sub + "원");
            }
        }
        System.out.println("---------------------");
        System.out.println("최종금액: " + total +"원");
    }




    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        MenuStore store = MenuStore.defaultStore();

        int total = 0;

        while(true) {
            System.out.println("\n------ 주문하세요 ------");
            // 동적으로 카테고리(개수/이름)가 늘어나도 자동 반영
            for (Map.Entry<Integer, Category> e : store.categories.entrySet()) {
                System.out.println(e.getKey() + ". " + e.getValue().name);
            }
            int checkoutNo = store.categories.size() + 1;
            int exitNo = store.categories.size() + 2;
            System.out.println(checkoutNo + ". 결제(영수증 출력 후 종료)");
            System.out.println(exitNo + ". 종료");

            int choice = readIntWithTimer(sc, "선택: ", 10);

            if (choice == checkoutNo) {
                printReceipt(store.allMenus());
                sc.close();
                return;
            } else if (choice == exitNo) {
                System.out.println("주문을 취소하고 종료합니다.");
                sc.close();
                return;
            } else if (store.categories.containsKey(choice)) {
                // 페이지당 5개씩 표시(필요 시 조정)
                total += handleCategory(sc, store.categories.get(choice), 5);
                System.out.println("현재합계: " + total + "원");
            } else {
                System.out.println("[안내] 올바른 번호를 선택해주세요.");
            }
        }
    }
}
