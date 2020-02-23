package com.jb;


import org.junit.Test;

import static com.jb.LBClass.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LBClassTest {
    @Test
    public void shouldRecognizeClass() {
        assertThat(LBClass.valueOf(1)).isEqualTo(LESSTHAN100kB);
        assertThat(LBClass.valueOf(132000)).isEqualTo(BETWEEN100kBAND1MB);
        assertThat(LBClass.valueOf(5 * 1024 * 1024)).isEqualTo(BETWEEN1MAND10MB);
        assertThat(LBClass.valueOf(50 * 1024 * 1024)).isEqualTo(BETWEEN10MBAND100MB);
        assertThat(LBClass.valueOf(500 * 1024 * 1024)).isEqualTo(BETWEEN100MBAND1GB);
    }
}
