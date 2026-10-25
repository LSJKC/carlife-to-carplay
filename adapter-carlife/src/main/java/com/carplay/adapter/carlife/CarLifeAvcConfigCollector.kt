package com.carplay.adapter.carlife

/** Keeps parameter sets that CarLife may send in separate stream packets. */
internal class CarLifeAvcConfigCollector {
    private var sps: ByteArray? = null
    private var pps: ByteArray? = null

    @Synchronized
    fun reset() {
        sps = null
        pps = null
    }

    @Synchronized
    fun offer(accessUnit: ByteArray): ByteArray? {
        val (newSps, newPps) = CarLifeProto.avcParameterSets(accessUnit)
        if (newSps != null) sps = newSps
        if (newPps != null) pps = newPps
        return CarLifeProto.avcConfig(sps, pps)
    }
}
