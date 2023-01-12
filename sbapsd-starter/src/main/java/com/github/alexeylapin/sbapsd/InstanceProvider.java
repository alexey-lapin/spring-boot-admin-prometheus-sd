package com.github.alexeylapin.sbapsd;

import com.github.alexeylapin.sbapsd.model.Item;
import reactor.core.publisher.Flux;

public interface InstanceProvider {

    Flux<Item> getItems();

}
