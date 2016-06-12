package com.kilic.kmeta.core.tests;

import org.junit.Before;
import org.junit.Test;

import com.kilic.kmeta.core.discriminator.CharSet;
import com.kilic.kmeta.core.meta.MAttribute;
import com.kilic.kmeta.core.meta.MClass;
import com.kilic.kmeta.core.meta.MModel;
import com.kilic.kmeta.core.meta.MPackage;
import com.kilic.kmeta.core.meta.MReference;
import com.kilic.kmeta.core.meta.Multiplicity;
import com.kilic.kmeta.core.syntax.CharSetExpr;
import com.kilic.kmeta.core.syntax.FeatureRef;
import com.kilic.kmeta.core.syntax.MultiplicityExpr;
import com.kilic.kmeta.core.syntax.SequenceExpr;
import com.kilic.kmeta.core.syntax.StringExpr;

public class CoreTests {
	MModel kmeta;
	MPackage core;
	MClass Expr;
	MClass PrimE;
	MClass LitE;
	MClass BinE;
	MReference BinE_l;
	MReference BinE_r;

	MClass PreE;
	MReference PreE_e;

	MClass SuffE;
	MReference SuffE_e;

	MClass NegE;

	MClass MulE;
	MClass AddE;
	MClass IncrE;

	MClass ParenE;
	MReference ParenE_e;

	MClass IntL;
	MAttribute IntL_v;

	@Before
	public void initModel() {
		kmeta = new MModel();
		kmeta.setName("kmeta");

		core = new MPackage();
		core.setName("core");
		core.setModel(kmeta);

		Expr = new MClass();
		Expr.setName("Expr");
		Expr.setPackage(core);

		PrimE = new MClass();
		PrimE.setName("PrimE");
		PrimE.addSuperClass(Expr);
		PrimE.setPackage(core);

		LitE = new MClass();
		LitE.setName("LitE");
		LitE.addSuperClass(PrimE);
		LitE.setPackage(core);

		BinE = new MClass();
		BinE.setName("BinE");
		BinE.addSuperClass(Expr);
		BinE.setPackage(core);

		BinE_l = new MReference();
		BinE_l.setTargetClass(PrimE);
		BinE_l.setContainerClass(BinE);

		BinE_r = new MReference();
		BinE_r.setTargetClass(Expr);
		BinE_r.setContainerClass(BinE);

		PreE = new MClass();
		PreE.setName("PreE");
		PreE.addSuperClass(Expr);
		PreE.setPackage(core);

		PreE_e = new MReference();
		PreE_e.setTargetClass(PrimE);
		PreE_e.setContainerClass(PreE);

		SuffE = new MClass();
		SuffE.setName("SuffE");
		SuffE.addSuperClass(Expr);
		SuffE.setPackage(core);
		SuffE_e = new MReference();
		SuffE_e.setTargetClass(PrimE);
		SuffE_e.setContainerClass(SuffE);

		NegE = new MClass();
		NegE.setName("NegE");
		NegE.addSuperClass(PreE);
		NegE.setPackage(core);
		NegE.setSyntax(new SequenceExpr(new StringExpr("-"), new FeatureRef(PreE_e)));

		MulE = new MClass();
		MulE.setName("MulE");
		MulE.addSuperClass(BinE);
		MulE.setPackage(core);
		MulE.setSyntax(new SequenceExpr(new FeatureRef(BinE_l), new StringExpr("*"), new FeatureRef(BinE_r)));

		AddE = new MClass();
		AddE.setName("AddE");
		AddE.addSuperClass(BinE);
		AddE.setPackage(core);
		AddE.setSyntax(new SequenceExpr(new FeatureRef(BinE_l), new StringExpr("+"), new FeatureRef(BinE_r)));

		IncrE = new MClass();
		IncrE.setName("IncrE");
		IncrE.addSuperClass(SuffE);
		IncrE.setPackage(core);
		IncrE.setSyntax(new SequenceExpr(new FeatureRef(SuffE_e), new StringExpr("++")));

		ParenE = new MClass();
		ParenE.setName("ParenE");
		ParenE.addSuperClass(PrimE);
		ParenE.setPackage(core);
		ParenE_e = new MReference();
		ParenE_e.setTargetClass(Expr);
		ParenE_e.setContainerClass(ParenE);
		ParenE.setSyntax(new SequenceExpr(new StringExpr("("), new FeatureRef(ParenE_e), new StringExpr(")")));

		IntL = new MClass();
		IntL.setName("IntL");
		IntL.addSuperClass(LitE);
		IntL.setPackage(core);
		IntL.setSyntax(new MultiplicityExpr(Multiplicity.ONEORMORE, new CharSetExpr(CharSet.DEC)));
	}

	@Test
	public void test() {
		// Predicter m = new Predicter();
		// m.match("(2+2)*x++ + y");
	}
}
