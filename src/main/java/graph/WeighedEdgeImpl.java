package graph;

/*
 * Copyright 2001-2020 by HireRight, Inc. All rights reserved.
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 */

public class WeighedEdgeImpl implements IWeighedEdge
{
	private final int from;
	private final int to;
	private final float weight;

	private WeighedEdgeImpl(int from, int to, float weight)
	{
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	public float getWeight()
	{
		return this.weight;
	}

	@Override public int getFrom()
	{
		return this.from;
	}

	@Override public int getTo()
	{
		return this.to;
	}


	public static IWeighedEdge newInstance(int from, int to, float weight)
	{
		return new WeighedEdgeImpl(from, to, weight);
	}
}
