package com.intellifusion.test;

public class FacadeTest {
    public static void main(String[] args) {

    }

}
class client1{


    public static void main(String[] args) {
        //Request request = null;
        //RequestFacade requestFacade = new RequestFacade(request);
        //requestFacade.getSession(true);
        System.out.println(unescape("韩 \\长,.峰 123"));
    }

    protected static String unescape(String s) {
        if (s == null) {
            return null;
        } else if (s.indexOf(92) == -1) {
            return s;
        } else {
            StringBuilder buf = new StringBuilder();

            for(int i = 0; i < s.length(); ++i) {
                char c = s.charAt(i);
                if (c != '\\') {
                    buf.append(c);
                } else {
                    ++i;
                    if (i >= s.length()) {
                        throw new IllegalArgumentException();
                    }

                    c = s.charAt(i);
                    buf.append(c);
                }
            }

            return buf.toString();
        }
    }

}


class Facade{
    subSystem1 system1 = new subSystem1();
    subSystem2 system2 = new subSystem2();
    subSystem3 system3 = new subSystem3();

    public void doSomethingFacade(){
        system1.method1();
        system2.method2();
        system3.method3();
    }

}

class subSystem1{
    public void method1(){
        System.out.println("subSystem1.method1......");
    }
}

class subSystem2{
    public void method2(){
        System.out.println("subSystem2.method2......");
    }
}

class subSystem3{
    public void method3(){
        System.out.println("subSystem3.method3......");
    }
}