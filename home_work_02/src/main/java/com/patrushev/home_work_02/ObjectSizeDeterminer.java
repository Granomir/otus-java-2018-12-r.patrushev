package com.patrushev.home_work_02;

import com.patrushev.home_work_02.factories.ObjectFactory;

public interface ObjectSizeDeterminer {

    long getMemorySizeOfObject(ObjectFactory objectFactory);
}
