import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jenetics.DoubleChromosome;
import org.jenetics.DoubleGene;
import org.jenetics.Genotype;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;

import com.codepoetics.protonpack.StreamUtils;

public class HelloWorld {
	
	static List<BigDecimal> its = listFrom(76, 44, 53, 74, 76);
	static BigDecimal itSumPower2 = its.stream().reduce(BigDecimal.ZERO, BigDecimal::add).pow(2);
	
	static List<BigDecimal> eq1 = listFrom(1, 2, 5, 4, 8);
	static List<BigDecimal> eq2 = listFrom(3, 3, 2, 1, 5);
	static List<BigDecimal> eq3 = listFrom(5, 2, 1, 4, 5);
	static List<BigDecimal> eq4 = listFrom(2, 5, 8, 7, 2);
	static List<BigDecimal> eq5 = listFrom(1, 5, 2, 3, 0);
	
	static List<BigDecimal> listFrom(int a, int b, int c, int d, int e) {
		return Stream.of(a, b, c, d, e)
				.map(BigDecimal::valueOf)
				.collect(Collectors.toList());
	}
	
	static BigDecimal operate(List<DoubleGene> genes, List<BigDecimal> datas) {
		Stream<BigDecimal> decimals = genes.stream()
				.map(DoubleGene::doubleValue)
				.map(BigDecimal::valueOf);
		
		return StreamUtils
				.zip(decimals, datas.stream(), (a, b) -> a.multiply(b))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
    private static BigDecimal fitnessEval(Genotype<DoubleGene> gt) {
    	List<DoubleGene> values = gt.getChromosome().stream().collect(Collectors.toList());
		BigDecimal firstEq = operate(values, eq1).pow(2);
    	BigDecimal secondEq = operate(values, eq2).pow(2);
    	BigDecimal thirdEq = operate(values, eq3).pow(2);
    	BigDecimal fourthEq = operate(values, eq4).pow(2);
    	BigDecimal fifthEq = operate(values, eq5).pow(2);
    	
		BigDecimal errorValue = firstEq.add(secondEq).add(thirdEq).add(fourthEq).add(fifthEq);
		System.out.println("PIOLA!!!!" + itSumPower2 + "     -     " + errorValue);
        return itSumPower2.subtract(errorValue);//.divide(itSumPower2);
    }
    
    public static void main(String[] args) {
        Factory<Genotype<DoubleGene>> gtf =
            Genotype.of(DoubleChromosome.of(-10000, 10000, 5));
 
        Engine<DoubleGene, BigDecimal> engine = Engine
            .builder(HelloWorld::fitnessEval, gtf)
            .build();
 
        Genotype<DoubleGene> result = engine.stream()
            .limit(100)
            .collect(EvolutionResult.toBestGenotype());
 
        System.out.println("Solution:\n" + result);
    }
}
