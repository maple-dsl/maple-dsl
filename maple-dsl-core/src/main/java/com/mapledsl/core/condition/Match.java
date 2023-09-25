package com.mapledsl.core.condition;

import com.mapledsl.core.model.Model;

/**
 * @author bofa1ex
 * @since 2023/08/28
 * @param <M>
 */
public interface Match<M extends Model<?>> extends Wrapper<Match<M>>, Condition<M, Match<M>>, Query<M, Sort<M>> {
}