package com.mapledsl.core.condition;

import com.mapledsl.core.model.Model;

public interface Fetch<M extends Model<?>> extends Wrapper<Fetch<M>>, Query<M, Sort<M>> {
}
