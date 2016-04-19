import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jenetics.DoubleChromosome;
import org.jenetics.DoubleGene;
import org.jenetics.Genotype;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.DoubleRange;
import org.jenetics.util.Factory;

public class HelloWorld {
	
	static List<BigDecimal> its = listFrom(76, 44, 53, 74, 76);
	static BigDecimal itSumPower2 = BigDecimal.valueOf(104329);
//	static BigDecimal itSumPower2 = its.stream().reduce(BigDecimal.ZERO, BigDecimal::add).pow(2);
	
	static List<BigDecimal> eq1 = listFrom(1, 2, 5, 4, 8);
	static List<BigDecimal> eq2 = listFrom(3, 3, 2, 1, 5);
	static List<BigDecimal> eq3 = listFrom(5, 2, 1, 4, 5);
	static List<BigDecimal> eq4 = listFrom(2, 5, 8, 7, 2);
	static List<BigDecimal> eq5 = listFrom(1, 5, 2, 3, 0);
	
	static List<BigDecimal> listFrom(int a, int b, int c, int d, int e) {
		return new ArrayList<>(Arrays.asList(BigDecimal.valueOf(a),BigDecimal.valueOf(b),BigDecimal.valueOf(c),BigDecimal.valueOf(d),BigDecimal.valueOf(e)));
	}	
	
	static BigDecimal operate(List<DoubleGene> genes, List<BigDecimal> datas) {
		return datas.get(0).multiply(BigDecimal.valueOf(genes.get(0).doubleValue()))
				.add(datas.get(1).multiply(BigDecimal.valueOf(genes.get(1).doubleValue())))
				.add(datas.get(2).multiply(BigDecimal.valueOf(genes.get(2).doubleValue())))
				.add(datas.get(3).multiply(BigDecimal.valueOf(genes.get(3).doubleValue())))
				.add(datas.get(4).multiply(BigDecimal.valueOf(genes.get(4).doubleValue())));
	}
	
    private static BigDecimal fitnessEval(Genotype<DoubleGene> gt) {
    	List<DoubleGene> values = gt.getChromosome().stream().collect(Collectors.toList());
		BigDecimal firstEq = operate(values, eq1).subtract(its.get(0));
    	BigDecimal secondEq = operate(values, eq2).subtract(its.get(1));
    	BigDecimal thirdEq = operate(values, eq3).subtract(its.get(2));
    	BigDecimal fourthEq = operate(values, eq4).subtract(its.get(3));
    	BigDecimal fifthEq = operate(values, eq5).subtract(its.get(4));
    	
		BigDecimal errorValue = firstEq.add(secondEq).add(thirdEq).add(fourthEq).add(fifthEq).pow(2);
		
		System.out.println("Valores: " + values);
		System.out.println("Funcion de aptitud: " + itSumPower2.subtract(errorValue).divide(itSumPower2, 2, RoundingMode.HALF_UP));
		System.out.println("Error: " + errorValue);
        return itSumPower2.subtract(errorValue).divide(itSumPower2, 2, RoundingMode.HALF_UP);
    }
    
    public static void main(String[] args) {
        Factory<Genotype<DoubleGene>> gtf =
            Genotype.of(DoubleChromosome.of(DoubleRange.of(1, 5), 5));
 
        Engine<DoubleGene, BigDecimal> engine = Engine
            .builder(HelloWorld::fitnessEval, gtf)
            .build();
 
        Genotype<DoubleGene> result = engine.stream()
            .limit(100)
            .collect(EvolutionResult.toBestGenotype());
 
        System.out.println("Solution:\n" + result);
    }
}
