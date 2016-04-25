import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jenetics.Genotype;
import org.jenetics.IntegerChromosome;
import org.jenetics.IntegerGene;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;

import com.codepoetics.protonpack.StreamUtils;

public class HelloWorld {
	
	static List<BigDecimal> its;
	
	static List<BigDecimal> eq1;
	static List<BigDecimal> eq2;
	static List<BigDecimal> eq3;
	static List<BigDecimal> eq4;
	static List<BigDecimal> eq5;
	
	private static BigDecimal realValue(List<IntegerGene> genes, List<BigDecimal> datas) {
		return StreamUtils
				.zip(genes.stream(), datas.stream(), (gen, data) -> data.multiply(BigDecimal.valueOf(gen.intValue())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private static BigDecimal errorValue(List<IntegerGene> genes, List<BigDecimal> datas, int index) {
		return realValue(genes, datas).subtract(its.get(index)).abs();
	}

	private static BigDecimal sumErrorValues(Genotype<IntegerGene> gt) {
    	List<IntegerGene> values = gt.getChromosome().stream().collect(Collectors.toList());

    	BigDecimal firstEq = errorValue(values, eq1, 0);
    	BigDecimal secondEq = errorValue(values, eq2, 1);
    	BigDecimal thirdEq = errorValue(values, eq3, 2);
    	BigDecimal fourthEq = errorValue(values, eq4, 3);
    	BigDecimal fifthEq = errorValue(values, eq5, 4);

    	BigDecimal errorValue = firstEq.add(secondEq).add(thirdEq).add(fourthEq).add(fifthEq);

		return errorValue;
	}

	private static BigDecimal sumTermValues() {
		return its.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
    private static BigDecimal fitnessEval(Genotype<IntegerGene> gt) {
    	BigDecimal sumTermValuesSquared = sumTermValues().pow(2);
    	BigDecimal sumErrorValuesSquared = sumErrorValues(gt).pow(2);
    	
    	if (sumTermValuesSquared.compareTo(sumErrorValuesSquared) == -1) return BigDecimal.ZERO;
    	
    	BigDecimal fitness = sumTermValuesSquared
    			.subtract(sumErrorValuesSquared) 
    			.divide(sumTermValuesSquared, 100, RoundingMode.HALF_DOWN);

		return fitness;
    }
    
	public static void main(String[] args) {
    	
       	BigDecimal v1 = BigDecimal.valueOf(1); 
    	BigDecimal w1 = BigDecimal.valueOf(2); 
    	BigDecimal x1 = BigDecimal.valueOf(5); 
    	BigDecimal y1 = BigDecimal.valueOf(4); 
    	BigDecimal z1 = BigDecimal.valueOf(8); 
    	BigDecimal ti1 = BigDecimal.valueOf(76);
    	eq1 = new ArrayList<>(Arrays.asList(v1, w1, x1, y1, z1));
    	
       	BigDecimal v2 = BigDecimal.valueOf(3); 
    	BigDecimal w2 = BigDecimal.valueOf(3); 
    	BigDecimal x2 = BigDecimal.valueOf(2); 
    	BigDecimal y2 = BigDecimal.valueOf(1); 
    	BigDecimal z2 = BigDecimal.valueOf(5); 
    	BigDecimal ti2 = BigDecimal.valueOf(44);
    	eq2 = new ArrayList<>(Arrays.asList(v2, w2, x2, y2, z2));
    	
       	BigDecimal v3 = BigDecimal.valueOf(5); 
    	BigDecimal w3 = BigDecimal.valueOf(2); 
    	BigDecimal x3 = BigDecimal.valueOf(1); 
    	BigDecimal y3 = BigDecimal.valueOf(4); 
    	BigDecimal z3 = BigDecimal.valueOf(5); 
    	BigDecimal ti3 = BigDecimal.valueOf(53);
    	eq3 = new ArrayList<>(Arrays.asList(v3, w3, x3, y3, z3));
    	
       	BigDecimal v4 = BigDecimal.valueOf(2); 
    	BigDecimal w4 = BigDecimal.valueOf(5); 
    	BigDecimal x4 = BigDecimal.valueOf(8); 
    	BigDecimal y4 = BigDecimal.valueOf(7); 
    	BigDecimal z4 = BigDecimal.valueOf(2); 
    	BigDecimal ti4 = BigDecimal.valueOf(74);
    	eq4 = new ArrayList<>(Arrays.asList(v4, w4, x4, y4, z4));
    	
       	BigDecimal v5 = BigDecimal.valueOf(1); 
    	BigDecimal w5 = BigDecimal.valueOf(5); 
    	BigDecimal x5 = BigDecimal.valueOf(2); 
    	BigDecimal y5 = BigDecimal.valueOf(3); 
    	BigDecimal z5 = BigDecimal.valueOf(0); 
    	BigDecimal ti5 = BigDecimal.valueOf(29);
    	eq5 = new ArrayList<>(Arrays.asList(v5, w5, x5, y5, z5));
    	
    	its = new ArrayList<>(Arrays.asList(ti1, ti2, ti3, ti4, ti5));
    	
        Factory<Genotype<IntegerGene>> gtf = Genotype.of(IntegerChromosome.of(-10, 10, 5));
        
        Engine<IntegerGene, BigDecimal> engine = Engine
            .builder(HelloWorld::fitnessEval, gtf)
            .build();
 
        Genotype<IntegerGene> result = engine.stream()
            .limit(10000)
            .collect(EvolutionResult.toBestGenotype());
 
        System.out.println("Solution:\n" + result);
    }
}
