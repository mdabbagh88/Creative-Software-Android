package cs.android.rpc;

import cs.android.lang.ServerRequest;
import cs.java.collections.List;

public interface ListRequest<Item> extends ServerRequest {
	List<Item> getList();
}
