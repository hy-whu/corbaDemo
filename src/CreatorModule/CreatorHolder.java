package CreatorModule;

/**
* CreatorModule/CreatorHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从creator.idl
* 2019年6月11日 星期二 下午08时52分56秒 CST
*/

public final class CreatorHolder implements org.omg.CORBA.portable.Streamable
{
  public Creator value = null;

  public CreatorHolder ()
  {
  }

  public CreatorHolder (Creator initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = CreatorHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    CreatorHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return CreatorHelper.type ();
  }

}
