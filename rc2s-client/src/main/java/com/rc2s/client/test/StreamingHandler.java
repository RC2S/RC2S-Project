package com.rc2s.client.test;

import com.rc2s.ejb.streaming.StreamingFacadeRemote;

public class StreamingHandler extends Thread
{
	private Streaming streaming;

	public StreamingHandler(StreamingFacadeRemote streamingEJB, String id, String media) throws Exception
	{
		this.streaming = new Streaming(streamingEJB, id, media);
	}

	@Override
	public void run()
	{
		synchronized (streaming) {
			streaming.start();
		}
	}

	public void pauseStreaming()
	{
		synchronized (streaming) {
			streaming.setStreamingState(Streaming.StreamingState.PAUSE);
			streaming.notifyAll();
		}
	}

	public void resumeStreaming()
	{
		synchronized (streaming) {
			streaming.setStreamingState(Streaming.StreamingState.PLAY);
			streaming.notifyAll();
		}
	}

	public void stopStreaming()
	{
		synchronized (streaming) {
			streaming.setStreamingState(Streaming.StreamingState.STOP);
			streaming.notifyAll();
		}
	}

	public boolean isPlaying()
	{
		return streaming.isPlaying();
	}

	public Streaming.StreamingState getStreamingState()
	{
		return streaming.getStreamingState();
	}
}
