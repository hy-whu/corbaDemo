package client;

import CreatorModule.Creator;
import CreatorModule.CreatorHelper;
import UserModule.User;
import UserModule.UserHelper;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;


public class TodoListClient {
    private Creator creator;
    private User user;
    private BufferedReader reader;
    private org.omg.CORBA.Object objRef;
    private NamingContextExt ncRef;

    public TodoListClient() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String[] args) {
        args = new String[2];
        args[0] = "-ORBInitialPort";
        args[1] = "1050";
        TodoListClient todoListClient = new TodoListClient();
        todoListClient.init(args);
        todoListClient.procedure();
    }


    private void init(String[] args) {

        System.out.println("客户端启动运行");


        //创建一个ORB实例
        ORB orb = ORB.init(args, null);

        //获取根名称上下文
        try {
            objRef = orb.resolve_initial_references("NameService");
        } catch (InvalidName e) {
            e.printStackTrace();
        }
        ncRef = NamingContextExtHelper.narrow(objRef);

        String name = "Creator";
        try {
            //通过ORB拿到server实例化好的Creator类
            creator = CreatorHelper.narrow(ncRef.resolve_str(name));
        } catch (NotFound e) {
            e.printStackTrace();
        } catch (CannotProceed e) {
            e.printStackTrace();
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
            e.printStackTrace();
        }


    }

    //与用户交互
    public void procedure() {
        String choice;
        String startTime, endTime, label;
        String index;

        try {
            while (true) {
                System.out.println("请选择你的操作\n1-注册\n2-登陆\n3-退出");
                choice = reader.readLine();

                switch (choice) {
                    case "1":
                        while (true) {
                            if (register()) {
                                System.out.println("注册成功");
                                break;
                            }
                        }
                        break;
                    case "2":
                        while (true) {
                            if (login()) {
                                System.out.println("登陆成功");
                                do {
                                    System.out.println("请选择操作\n1、增加代办事件\n2、查询代办事件\n3、显示所有代办事件\n4、删除代办事件\n5、删除所有代办事件\n6、登出");

                                    choice = reader.readLine();

                                    switch (choice) {
                                        case "1":
                                            System.out.println("请输入起始时间，按回车结束");
                                            startTime = reader.readLine();
                                            System.out.println("请输入结束时间，按回车结束");
                                            endTime = reader.readLine();
                                            System.out.println("请输入备注");
                                            label = reader.readLine();
                                            if (user.add(startTime, endTime, label)) {
                                                System.out.println("添加成功");
                                            } else {
                                                System.out.println("添加失败");
                                            }
                                            break;
                                        case "2":
                                            System.out.println("请输入起始时间，按回车结束");
                                            startTime = reader.readLine();
                                            System.out.println("请输入结束时间，按回车结束");
                                            endTime = reader.readLine();
                                            System.out.println(user.query(startTime, endTime));
                                            break;
                                        case "3":
                                            System.out.println(user.show());
                                            break;
                                        case "4":
                                            System.out.println("请输入删除事件的编号");
                                            index = reader.readLine();
                                            if (user.delete(index)) {
                                                System.out.println("删除成功");
                                            } else {
                                                System.out.println("删除失败");
                                            }
                                            break;
                                        case "5":
                                            if (user.clear()) {
                                                System.out.println("您已成功删除所有事件");
                                            } else {
                                                System.out.println("代办事件为空");
                                            }
                                            break;
                                    }
                                } while (!choice.equals("6"));
                                break;
                            } else {
                                System.out.println("登陆失败");
                                break;
                            }
                        }
                        break;
                    case "3":
                        return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //注册
    private boolean register() {
        String username, password;

        try {
            System.out.println("请输入注册用户名:");
            username = reader.readLine();
            System.out.println("请输入密码:");
            password = reader.readLine();
            if (creator.register(username, password)) {
                System.out.println("注册成功");
                return true;
            } else {
                System.out.println("注册失败");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //登录
    private boolean login() {
        String username, password;

        try {
            System.out.println("请输入用户名");
            username = reader.readLine();
            System.out.println("请输入密码");
            password = reader.readLine();
            if (creator.login(username, password)) {
                try {
                    //通过ORB拿到server实例化好的User类
                    user = UserHelper.narrow(ncRef.resolve_str(username));
                } catch (NotFound e) {
                    e.printStackTrace();
                } catch (CannotProceed e) {
                    e.printStackTrace();
                } catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
