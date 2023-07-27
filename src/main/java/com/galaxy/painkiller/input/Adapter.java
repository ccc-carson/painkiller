package com.galaxy.painkiller.input;

import com.galaxy.painkiller.model.Action;
import org.apache.http.client.methods.HttpUriRequest;

public interface Adapter<S> {

   Action adapt(S request);

}
