package org.ubercraft.statsd;

import static org.ubercraft.statsd.StatsdStatType.COUNTER;
import static org.ubercraft.statsd.StatsdStatType.GAUGE;
import static org.ubercraft.statsd.StatsdStatType.TIMER;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * This class provides a statsd/slf4j-like interface for logging statsd stats. Each instance wraps a given
 * {@link Logger} instance;
 * <p/>
 * For any instance of this class, the name of the logger corresponds to the statsd key that will be logged by it. Use
 * the <code>xxxCount(...)</code>, <code>xxxTime(...)</code> and <code>xxxStat(...)</code> methods to log stats as
 * desired.
 * <p/>
 * This class has three <code>statXxx(...)</code> methods, which can be overridden in order to customise exactly how the
 * log message is constructed prior to being logged. The expectation is that the message logged will be understandable
 * by whatever appender has been attached to this logger.
 * 
 * @see StatsdLoggerFactory
 */
public class StatsdLoggerImpl implements StatsdLogger, Serializable {

    private static final long serialVersionUID = 6548797032077199054L;

    protected final Logger logger;

    public StatsdLoggerImpl(Logger logger) {
        this.logger = logger;
    }

    public String getName() {
        return logger.getName();
    }

    @Override
    public String toString() {
        return "StatsdLoggerImpl[" + getName() + "]";
    }

    // deserialization
    protected Object readResolve() throws ObjectStreamException {
        return StatsdLoggerFactory.getLogger(getName());
    }

    //
    // Info level
    //

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void infoCount() {
        infoCount(1);
    }

    @Override
    public void infoCount(int count) {
        infoCount(count, 1.0D);
    }

    @Override
    public void infoCount(double sampleRate) {
        infoCount(1, sampleRate);
    }

    @Override
    public void infoCount(int count, double sampleRate) {
        infoStat(COUNTER, count, sampleRate);
    }

    @Override
    public void infoTime(long millis) {
        infoTime(millis, 1.0);
    }

    @Override
    public void infoTime(long millis, double sampleRate) {
        infoStat(TIMER, millis, sampleRate);
    }

    @Override
    public void infoGauge(int value) {
        infoStat(GAUGE, value, 1.0);
    }

    @Override
    public void infoStat(StatsdStatType type, long value, double sampleRate) {
        if (isInfoEnabled()) logger.info( //
                statMarker(type, value, sampleRate), //
                statMessage(type, value, sampleRate), //
                statArgs(type, value, sampleRate));
    }

    //
    // Debug level
    //

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debugCount() {
        debugCount(1);
    }

    @Override
    public void debugCount(int count) {
        debugCount(count, 1.0D);
    }

    @Override
    public void debugCount(double sampleRate) {
        debugCount(1, sampleRate);
    }

    @Override
    public void debugCount(int count, double sampleRate) {
        debugStat(COUNTER, count, sampleRate);
    }

    @Override
    public void debugTime(long millis) {
        debugTime(millis, 1.0);
    }

    @Override
    public void debugTime(long millis, double sampleRate) {
        debugStat(TIMER, millis, sampleRate);
    }

    @Override
    public void debugGauge(int value) {
        debugStat(GAUGE, value, 1.0);
    }

    @Override
    public void debugStat(StatsdStatType type, long value, double sampleRate) {
        if (isDebugEnabled()) logger.debug( //
                statMarker(type, value, sampleRate), //
                statMessage(type, value, sampleRate), //
                statArgs(type, value, sampleRate));
    }

    //
    // Trace level
    //

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void traceCount() {
        traceCount(1);
    }

    @Override
    public void traceCount(int count) {
        traceCount(count, 1.0D);
    }

    @Override
    public void traceCount(double sampleRate) {
        traceCount(1, sampleRate);
    }

    @Override
    public void traceCount(int count, double sampleRate) {
        traceStat(COUNTER, count, sampleRate);
    }

    @Override
    public void traceTime(long millis) {
        traceTime(millis, 1.0);
    }

    @Override
    public void traceTime(long millis, double sampleRate) {
        traceStat(TIMER, millis, sampleRate);
    }

    @Override
    public void traceGauge(int value) {
        traceStat(GAUGE, value, 1.0);
    }

    @Override
    public void traceStat(StatsdStatType type, long value, double sampleRate) {
        if (isTraceEnabled()) logger.trace( //
                statMarker(type, value, sampleRate), //
                statMessage(type, value, sampleRate), //
                statArgs(type, value, sampleRate));
    }

    //
    // override-able methods for creating the actual parameters used to call the underlying logger with
    // (the default behaviour suits logback)
    //

    protected Marker statMarker(StatsdStatType type, long value, double sampleRate) {
        return null;
    }

    protected String statMessage(StatsdStatType type, long value, double sampleRate) {
        return null;
    }

    protected Object[] statArgs(StatsdStatType type, long value, double sampleRate) {
        return new Object[] {
                type, value, sampleRate
        };
    }
}
