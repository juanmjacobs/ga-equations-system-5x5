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
import org.jenetics.util.Factory;

public class HelloWorld {
	
	static List<BigDecimal> its;
	
	static List<BigDecimal> eq1;
	static List<BigDecimal> eq2;
	static List<BigDecimal> eq3;
	static List<BigDecimal> eq4;
	static List<BigDecimal> eq5;
	
	private static BigDecimal term(List<DoubleGene> genes, List<BigDecimal> datas, int index) {
		BigDecimal value = datas.get(index);
		DoubleGene gene = genes.get(index);
		BigDecimal geneValue = BigDecimal.valueOf(gene.doubleValue());
		return value.multiply(geneValue);
	}
	
	private static BigDecimal realValue(List<DoubleGene> genes, List<BigDecimal> datas) {
		return term(genes, datas, 0)
          .add(term(genes, datas, 1))
          .add(term(genes, datas, 2))
          .add(term(genes, datas, 3))
          .add(term(genes, datas, 4));
	}

	private static BigDecimal errorValue(List<DoubleGene> genes, List<BigDecimal> datas, int index) {
		return realValue(genes, datas).subtract(its.get(index)).abs();
	}

	private static BigDecimal sumErrorValues(Genotype<DoubleGene> gt) {
    	List<DoubleGene> values = gt.getChromosome().stream().collect(Collectors.toList());

    	BigDecimal firstEq = errorValue(values, eq1, 0);
    	BigDecimal secondEq = errorValue(values, eq2, 1);
    	BigDecimal thirdEq = errorValue(values, eq3, 2);
    	BigDecimal fourthEq = errorValue(values, eq4, 3);
    	BigDecimal fifthEq = errorValue(values, eq5, 4);

    	BigDecimal errorValue = firstEq.add(secondEq).add(thirdEq).add(fourthEq).add(fifthEq);

    	System.out.println("ERROR VALUES: " + scale(errorValue));

		return errorValue;
	}

	private static BigDecimal sumTermValues() {
		return its.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	private static BigDecimal scale(BigDecimal bd) {
		return bd.setScale(4, RoundingMode.HALF_DOWN);
	}
	
    private static BigDecimal fitnessEval(Genotype<DoubleGene> gt) {
    	BigDecimal sumTermValuesSquared = sumTermValues().pow(2);
    	BigDecimal sumErrorValuesSquared = sumErrorValues(gt).pow(2);
    	
    	BigDecimal actitudeFunction = sumTermValuesSquared
    			.subtract(sumErrorValuesSquared) 
    			.divide(sumTermValuesSquared, 2, RoundingMode.HALF_DOWN);

    	System.out.println("SUMATORIA TERMINOS INDEPENDIENTES AL CUADRADO: " + scale(sumErrorValuesSquared));
    	System.out.println("SUMATORIA ERRORES AL CUADRADO: " + scale(sumErrorValuesSquared));
    	System.out.println("FUNCION DE ACTITUD: " + scale(actitudeFunction));
    	
		return actitudeFunction;
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
    	BigDecimal ti5 = BigDecimal.valueOf(76);
    	eq5 = new ArrayList<>(Arrays.asList(v5, w5, x5, y5, z5));
    	
    	its = new ArrayList<>(Arrays.asList(ti1, ti2, ti3, ti4, ti5));
    	
        Factory<Genotype<DoubleGene>> gtf = Genotype.of(DoubleChromosome.of(1, 5, 5));
 
        Engine<DoubleGene, BigDecimal> engine = Engine
            .builder(HelloWorld::fitnessEval, gtf)
            .build();
 
        Genotype<DoubleGene> result = engine.stream()
            .limit(1)
            .collect(EvolutionResult.toBestGenotype());
 
        System.out.println("Solution:\n" + result);
    }
}
