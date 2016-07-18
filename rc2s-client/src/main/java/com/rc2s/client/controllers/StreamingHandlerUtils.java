package com.rc2s.client.controllers;

import com.rc2s.ejb.streaming.StreamingFacadeRemote;

public class StreamingHandlerUtils
{
	private StreamingUtils streaming;

	public StreamingHandlerUtils(StreamingFacadeRemote streamingEJB, String id, String media) throws Exception
	{
		this.streaming = new StreamingUtils(streamingEJB, id, media);
		this.streaming.setDaemon(true);
	}

	public void start()
	{
		synchronized (streaming) {
			streaming.start();
		}
	}

	public void pauseStreaming()
	{
		synchronized (streaming) {
			streaming.setStreamingState(StreamingUtils.StreamingState.PAUSE);
			streaming.notifyAll();
		}
	}

	public void resumeStreaming()
	{
		synchronized (streaming) {
			streaming.setStreamingState(StreamingUtils.StreamingState.PLAY);
			streaming.notifyAll();
		}
	}

	public void stopStreaming()
	{
		synchronized (streaming) {
			streaming.setStreamingState(StreamingUtils.StreamingState.STOP);
			streaming.notifyAll();

			System.err.println("------- Waiting for the current StreamingUtils process to stop... ------");
			try {
				streaming.join();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			System.err.println("------ StreamingUtils has ended! ------");
		}
	}

	public boolean isPlaying()
	{
		return streaming.isPlaying();
	}

	public StreamingUtils.StreamingState getStreamingState()
	{
		return streaming.getStreamingState();
	}

	public StreamingUtils getSlave()
	{
		return streaming;
	}
}
