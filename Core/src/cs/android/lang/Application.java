package cs.android.lang;


public interface Application extends IsContext {

	String name();

	CSLogger logger();

	String cacheDir();
}
