/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.io.direct;

public interface CustomDataSerializable {

    void readCustomData(CustomDataManager manager);

    CustomDataManager writeCustomData();

}
