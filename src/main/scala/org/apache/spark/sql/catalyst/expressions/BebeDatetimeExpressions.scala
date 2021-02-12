package org.apache.spark.sql.catalyst.expressions

import org.apache.spark.sql.catalyst.expressions.codegen.{CodegenContext, ExprCode}
import org.apache.spark.sql.catalyst.util.{BebeDateTimeUtils, DateTimeUtils}
import org.apache.spark.sql.types.{AbstractDataType, DataType, DateType}

/**
 * Returns the first day of the month which the date belongs to.
 */
@ExpressionDescription(
  usage = "_FUNC_(date) - Returns the first day of the month which the date belongs to.",
  examples = """
    Examples:
      > SELECT _FUNC_('2009-01-12');
       2009-01-01
  """,
  group = "datetime_funcs",
  since = "3.1.0")
case class BeginningOfMonth(startDate: Expression) extends UnaryExpression with ImplicitCastInputTypes {
  override def child: Expression = startDate

  override def inputTypes: Seq[AbstractDataType] = Seq(DateType)

  override def dataType: DataType = DateType

  override def nullSafeEval(date: Any): Any = {
    BebeDateTimeUtils.getFirstDayOfMonth(date.asInstanceOf[Int])
  }

  override protected def doGenCode(ctx: CodegenContext, ev: ExprCode): ExprCode = {
    val dtu = BebeDateTimeUtils.getClass.getName.stripSuffix("$")
    defineCodeGen(ctx, ev, sd => s"$dtu.getFirstDayOfMonth($sd)")
  }

  override def prettyName: String = "beginning_of_month"
}

