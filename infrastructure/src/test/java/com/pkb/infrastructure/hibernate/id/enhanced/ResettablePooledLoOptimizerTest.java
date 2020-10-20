package com.pkb.infrastructure.hibernate.id.enhanced;

import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static com.pkb.infrastructure.hibernate.id.enhanced.ResettablePooledLoOptimizer.OPTIMIZER_CANONICAL_NAME;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;

import org.hibernate.id.IdentifierGeneratorHelper;
import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.id.enhanced.AccessCallback;
import org.hibernate.id.enhanced.Optimizer;
import org.hibernate.id.enhanced.OptimizerFactory;
import org.junit.Test;

public class ResettablePooledLoOptimizerTest {

    @Test
    public void optimizerFoundByHibernate() {
        Optimizer optimizer = OptimizerFactory.buildOptimizer(OPTIMIZER_CANONICAL_NAME, Long.class, 50, -1);

        assertThat(optimizer, instanceOf(ResettablePooledLoOptimizer.class));
    }

    @Test
    public void optimizerBasicUsage() {
        SourceMock sequence = new SourceMock(1, 3);
        Optimizer optimizer = OptimizerFactory.buildOptimizer(ResettablePooledLoOptimizer.class.getCanonicalName(), Long.class, 3, 1);

        assertEquals(0, sequence.getTimesCalled());
        assertEquals(-1, sequence.getCurrentValue());

        Long next = (Long)optimizer.generate(sequence);
        assertEquals(1, next.intValue());
        assertEquals(1, sequence.getTimesCalled());
        assertEquals(1, sequence.getCurrentValue());

        next = (Long)optimizer.generate(sequence);
        assertEquals(2, next.intValue());
        assertEquals(1, sequence.getTimesCalled());
        assertEquals(1, sequence.getCurrentValue());

        next = (Long)optimizer.generate(sequence);
        assertEquals(3, next.intValue());
        assertEquals(1, sequence.getTimesCalled());
        assertEquals(1, sequence.getCurrentValue());

        next = (Long)optimizer.generate(sequence);
        assertEquals(4, next.intValue());
        assertEquals(2, sequence.getTimesCalled());
        assertEquals(4, sequence.getCurrentValue());
    }

    @Test
    public void optimizerResetCausesIncrementEarlier() {
        SourceMock sequence = new SourceMock(1, 3);
        ResettablePooledLoOptimizer optimizer = (ResettablePooledLoOptimizer)OptimizerFactory.buildOptimizer(
                ResettablePooledLoOptimizer.class.getCanonicalName(),
                Long.class,
                3,
                1);

        assertEquals(0, sequence.getTimesCalled());
        assertEquals(-1, sequence.getCurrentValue());

        Long next = (Long)optimizer.generate(sequence);
        assertEquals(1, next.intValue());
        assertEquals(1, sequence.getTimesCalled());
        assertEquals(1, sequence.getCurrentValue());

        ResettablePooledLoOptimizer.clearStateGlobal();

        next = (Long)optimizer.generate(sequence);
        assertEquals(4, next.intValue());
        assertEquals(2, sequence.getTimesCalled());
        assertEquals(4, sequence.getCurrentValue());

        next = (Long)optimizer.generate(sequence);
        assertEquals(5, next.intValue());
        assertEquals(2, sequence.getTimesCalled());
        assertEquals(4, sequence.getCurrentValue());
    }

    /**
     * Largely pulled from hibernate-core's org.hibernate.id.enhanced.OptimizerUnitTest.SourceMock
     */
    private static class SourceMock implements AccessCallback {
        private IdentifierGeneratorHelper.BasicHolder value = new IdentifierGeneratorHelper.BasicHolder(Long.class);
        private long initialValue;
        private int increment;
        private int timesCalled ;

        private SourceMock(long initialValue, int increment) {
            this(initialValue, increment, 0 );
        }

        private SourceMock(long initialValue, int increment, int timesCalled) {
            this.increment = increment;
            this.timesCalled = timesCalled;

            if (timesCalled == 0) {
                this.value.initialize( -1 );
                this.initialValue = initialValue;
            }
            else {
                this.value.initialize( initialValue );
                this.initialValue = 1;
            }
        }

        @Override
        public IntegralDataTypeHolder getNextValue() {
            try {
                if (timesCalled == 0) {
                    initValue();
                    return value.copy();
                }
                else {
                    return value.add(increment).copy();
                }
            }
            finally {
                timesCalled++;
            }
        }

        @Override
        public String getTenantIdentifier() {
            return "test";
        }

        private void initValue() {
            this.value.initialize(initialValue);
        }

        private int getTimesCalled() {
            return timesCalled;
        }

        private long getCurrentValue() {
            return value == null ? -1 : value.getActualLongValue();
        }
    }

}
