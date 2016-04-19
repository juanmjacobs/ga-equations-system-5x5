import java.math.BigDecimal;
import java.util.stream.Stream;

import org.jenetics.DoubleChromosome;
import org.jenetics.DoubleGene;
import org.jenetics.Genotype;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;

import com.codepoetics.protonpack.StreamUtils;

 
public class HelloWorld {
	
	static int equation[][] = {
            {1,2,5,4,8},
            {3,3,2,1,5},
            {5,2,1,4,5},
            {2,5,8,7,2},
            {1,5,2,3,0}
			};
	static Stream<BigDecimal> eq1 = Stream.of(1, 2, 5, 4, 8).map(BigDecimal::valueOf);
	static Stream<BigDecimal> eq2 = Stream.of(3, 3, 2, 1, 5).map(BigDecimal::valueOf);
	static Stream<BigDecimal> eq3 = Stream.of(5, 2, 1, 4, 5).map(BigDecimal::valueOf);
	static Stream<BigDecimal> eq4 = Stream.of(2, 5, 8, 7, 2).map(BigDecimal::valueOf);
	static Stream<BigDecimal> eq5 = Stream.of(1, 5, 2, 3, 0).map(BigDecimal::valueOf);
	
	static BigDecimal operate(Stream<DoubleGene> genes, Stream<BigDecimal> data) {
		return StreamUtils
				.zip(genes.map(DoubleGene::doubleValue), data, (a, b) -> BigDecimal.valueOf(a).multiply(b))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	static Stream<BigDecimal> its = Stream.of(76, 44, 53, 74, 76).map(BigDecimal::valueOf);
  	static BigDecimal itSumPower2 = its.reduce(BigDecimal.ZERO, BigDecimal::add).pow(2);
	
    private static BigDecimal fitnessEval(Genotype<DoubleGene> gt) {
    	Stream<DoubleGene> values = gt.getChromosome().stream();
		BigDecimal firstEq = operate(values, eq1).pow(2);
//    	BigDecimal secondEq = operate(values, eq2).pow(2);
//    	BigDecimal thirdEq = operate(values, eq3).pow(2);
//    	BigDecimal fourthEq = operate(values, eq4).pow(2);
//    	BigDecimal fifthEq = operate(values, eq5).pow(2);
    	
		BigDecimal errorValue = firstEq;//.add(secondEq).add(thirdEq).add(fourthEq).add(fifthEq);
        return itSumPower2.subtract(errorValue).divide(itSumPower2);
    }
    
    
 
    public static void main(String[] args) {
        Factory<Genotype<DoubleGene>> gtf =
            Genotype.of(DoubleChromosome.of(Double.MIN_VALUE, Double.MAX_VALUE, 5));
 
        Engine<DoubleGene, BigDecimal> engine = Engine
            .builder(HelloWorld::fitnessEval, gtf)
            .build();
 
        Genotype<DoubleGene> result = engine.stream()
            .limit(100)
            .collect(EvolutionResult.toBestGenotype());
 
        System.out.println("Solution:\n" + result);
    }
}
