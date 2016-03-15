package generator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionAdapter<T> {
	public T obj;
	public ActionAdapter(T t) {
		obj = t;
	}
}
