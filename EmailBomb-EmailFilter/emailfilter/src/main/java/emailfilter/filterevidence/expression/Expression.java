package emailfilter.filterevidence.expression;

import emailfilter.filterevidence.expression.expression.Compare;
import emailfilter.filterevidence.expression.expression.Conjection;
import emailfilter.filterevidence.expression.expression.Content;
import emailfilter.filterevidence.expression.expression.Operation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface Expression {
    String Comparison() default "";
    Compare Compare() default Compare.EQUAL;
    Conjection Conjection() default Conjection.AND;
    Content Content() default Content.MAIN;
    Operation Operation() default Operation.D;
}
