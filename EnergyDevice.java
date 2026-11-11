package net.indtech.energy;

/**
 * Anything that can sit on the cable network. Returns how much energy (EU) was actually accepted.
 */
public interface EnergyDevice {
    int receiveEnergy(int amount);
}
