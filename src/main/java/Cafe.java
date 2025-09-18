
import java.util.*;
class Menu{
    String name;
    int price;
    int cnt;//개수
    Menu(String name, int price){
        this.name = name;
        this.price = price;
        this.cnt = 0;
    }// 인텔리제이 아이디어
}

class Coffee extends Menu{
  Coffee(String name, int price){ super(name, price); }
}

class Bread extends Menu{
   Bread(String name, int price){ super(name, price); }
}



public class Cafe {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Coffee iceAmericano = new Coffee("아이스 아메라카노", 3000);
        Coffee lattee = new Coffee("라떼",3500);
        Coffee hotChoco = new Coffee("핫초코",4000);

        Bread sbread = new Bread("식빵",3000);
        Bread muffin = new Bread("머핀",3500);
        Bread cake = new Bread("케이크",4500);

        int total = 0;

        while(true){
            System.out.println("------주문하세요------");
            System.out.println("1. 커피");
            System.out.println("2. 빵");
            System.out.println("3. 종료");
            System.out.println("선택");
            int choice = sc.nextInt();

            switch(choice) {


                case 1://커피선택
                    while (true) {
                        System.out.println("커피메뉴");
                        System.out.println("1. " + iceAmericano.name + " " + iceAmericano.price + "원");
                        System.out.println("2. " + lattee.name + " " + lattee.price + "원");
                        System.out.println("3. " + hotChoco.name + " " + hotChoco.price + "원");
                        System.out.println("4. 뒤로가기");
                        System.out.println("선택하세요");
                        int choice2 = sc.nextInt();
                        switch (choice2) {

                            case 1: //아아 선택
                                System.out.println("개수 : ");
                                int count1 = sc.nextInt();
                                iceAmericano.cnt += count1;//iceAmericano.cnt = iceAmericano.cnt + count;
                                // 아아 개수 계산
                                total += iceAmericano.price * count1;
                                System.out.println(iceAmericano.name + " " + count1 + " 개 총 개수 " + total + "원");
                                break;

                            case 2://   라떼 선택
                                System.out.println("개수 : ");
                                int count2 = sc.nextInt();
                                lattee.cnt += count2;//lattee.cnt = lattee.cnt + count;
                                // 아아 개수 계산
                                total += lattee.price * count2;
                                System.out.println(lattee.name + " " + count2 + " 개 총 개수 " + total + "원");
                                break;

                            case 3: // 핫초코 선택
                                System.out.println("개수 : ");
                                int count3 = sc.nextInt();
                                hotChoco.cnt += count3;//hotChoco.cnt = hotChoco.cnt + count;
                                // 아아 개수 계산
                                total += hotChoco.price * count3;
                                System.out.println(hotChoco.name + " " + count3 + " 개 총 개수 " + total + "원");
                                break;


                            case 4:
                                break;
                        }
                        if(choice2 == 4){
                            break;
                        }
                    }
                    break;

                case 2:// 빵 선택
                    while (true){
                        System.out.println("빵메뉴");
                    System.out.println("1. " + sbread.name + " " + sbread.price + "원");
                    System.out.println("2. " + muffin.name + " " + muffin.price + "원");
                    System.out.println("3. " + cake.name + " " + cake.price + "원");
                    System.out.println("4. 뒤로가기");
                    System.out.println("선택하세요");
                    int choice3 = sc.nextInt();


                    switch (choice3) {
                        case 1:// 식빵 선택
                            System.out.println("개수 : ");
                            int count1 = sc.nextInt();
                            sbread.cnt += count1;
                            total += sbread.price * count1;
                            System.out.println(sbread.name + " " + count1 + " 개 총 개수 " + total + "원");
                            break;

                        case 2: // 머핀 선택
                            System.out.println("개수 : ");
                            int count2 = sc.nextInt();
                            muffin.cnt += count2;
                            total += muffin.price * count2;
                            System.out.println(muffin.name + " " + count2 + " 개 총 개수 " + total + "원");
                            break;


                        case 3: // cake 선택
                            System.out.println("개수 : ");
                            int count3 = sc.nextInt();
                            cake.cnt += count3;
                            total += cake.price * count3;
                            System.out.println(cake.name + " " + count3 + " 개 총 개수 " + total + "원");
                            break;


                        case 4:
                            break;
                    }
                    if(choice3 == 4){
                        break;
                    }
            }
                    break;

                case 3: // 최종
                    System.out.println("최종 금액" + total + "원");
                    break;

            }
        }

    }
}
