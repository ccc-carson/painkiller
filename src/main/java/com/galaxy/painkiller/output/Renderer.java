package com.galaxy.painkiller.output;

public interface Renderer<E,R> {

    E render(R action);
}
