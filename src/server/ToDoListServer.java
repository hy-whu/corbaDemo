package server;

import CreatorModule.Creator;
import CreatorModule.CreatorHelper;
import CreatorModule.CreatorImpl;
import UserModule.User;
import UserModule.UserHelper;
import UserModule.UserImpl;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;


public class ToDoListServer {
    private ORB orb;
    private POA rootPOA;
    private org.omg.CORBA.Object ref;
    private NamingContextExt ncRef;

    public static void main(String[] args) {
        args = new String[2];
        args[0] = "-ORBInitialPort";
        args[1] = "1050";

        ToDoListServer toDoListServer = new ToDoListServer();
        toDoListServer.init(args);
    }

    //初始化,注册Creator到服务中
    private void init(String[] args) {
        try {


            orb = ORB.init(args, null);

            org.omg.CORBA.Object obj = orb.resolve_initial_references("RootPOA");
            rootPOA = POAHelper.narrow(obj);

            org.omg.PortableServer.POAManager manager = rootPOA.the_POAManager();

            CreatorImpl creatorImpl = new CreatorImpl();
            creatorImpl.setToDoListServer(this);

            ref = rootPOA.servant_to_reference(creatorImpl);
            Creator creatorhref = CreatorHelper.narrow(ref);


            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            ncRef = NamingContextExtHelper.narrow(objRef);


            String name = "Creator";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, creatorhref);

            manager.activate();
            System.out.println("服务器完成初始化");

            //启动线程服务,等待客户端调用
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //对用户名进行注册服务
    public void registerUserName(String name) {
        try {

            UserImpl userImpl = new UserImpl(name);
            userImpl.setORB(orb);

            ref = rootPOA.servant_to_reference(userImpl);
            User userhref = UserHelper.narrow(ref);
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, userhref);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
