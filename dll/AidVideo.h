#pragma once
#pragma warning(disable: 4996)

#define _WIN32_WINNT 0x0600

#include <boost/cstdint.hpp>
extern "C" {
#pragma warning(push)
#pragma warning(disable: 4244)
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libavutil/opt.h>
#pragma warning(pop)
}

#include <boost/asio.hpp>
#include <boost/chrono.hpp>
#include <vector>

struct TxvVideoFrm;
class AidParameters;

struct AidImage
{
	AidImage() : data(NULL) {}

	AidImage(
		unsigned char* buffer,
		int step,
		int width,
		int height,
		int cn
	)
		: step(step)
		, width(width)
		, height(height)
		, cn(cn)
	{
		unsigned int ds = GetDataSize();
		//std::cout << "malloc" << std::endl;
		data = (unsigned char*)malloc(ds);
		memcpy(data, buffer, ds);
	}

	// clone
	AidImage(const AidImage& other)
	{
		step = other.step;
		width = other.width;
		height = other.height;
		cn = other.cn;
		data = other.data;
	}

	~AidImage()
	{
	}

	void ReleaseImage()
	{
		//std::cout << "free" << std::endl;
		free(data);
	}

	unsigned int GetDataSize()
	{
		return step * height;
	}

	unsigned char* data;
	int step;
	int width;
	int height;
	int cn;
};


class AidVideo
{
	typedef std::vector<char> BufferType;
	
#pragma pack(push, 1)
	struct TxvVideoFrm
	{
		unsigned char		mediaType;	// 1: video
		unsigned char		encoding;	// 1:MPEG4, 2:H264
		unsigned char		frameType;	// 1:I-frame, 2:P-frame, 3:B-frame
		unsigned long long	timeStamp;	// in 1/10 us
		unsigned short		width;
		unsigned short		height;
	};
#pragma pack(pop)

public:

	volatile bool			m_isVGW;
	volatile bool			m_isNVR;

	struct VideoFrame
	{
		VideoFrame()
			: width(0)
			, buffer(NULL)
		{}

		VideoFrame(
			int width,
			int height,
			int colorCount,
			int lineSize,
			unsigned char* buffer
		)
			: width(width)
			, height(height)
			, colorCount(colorCount)
			, lineSize(lineSize)
			, buffer(buffer)
		{}

		int width, currwidth;
		int height, currheight;
		int colorCount;
		int lineSize;
		unsigned char* buffer;
	};

	unsigned int m_nOpenVideo;

	AidVideo();
	~AidVideo(void);
	bool Open(const std::string& url);
	void Close();
	bool GrabFrame();
	bool RetrieveFrame();
	//void SleepUntil(boost::chrono::system_clock::time_point time);
	const VideoFrame& GetDecodedFrame() { return m_videoFrame; }
	double GetFps();
	unsigned int GetChid() { return m_chid; }
	void SetChid(unsigned int cid) { m_chid = cid; m_isChidSet = true; }
	unsigned int GetWidth() { return m_videoFrame.width; }
	unsigned int GetHeight() { return m_videoFrame.height; }

	/**
	* Returns last frame timestamp in 1/10 microseconds
	*/
	unsigned long long GetFrameTimestamp();

	bool IsLive() { return m_isLive; }

	std::string GetContinuationURL();

	unsigned int GetAliveCounter() { return m_aliveCounter; }

	static std::string FormatTimestamp(unsigned long long utcMs);

	// used for frame caching
	const AVFrame* GetCurrentFrame();
	AVFrame *CloneCurrentFrame();
	bool RetrieveFrame(AVFrame* frame);
	void DeleteFrame(AVFrame** frame);

	bool IsValidFrame() { return m_validFrame; }
	int GetFramePeriodMs() { return m_framePeriodMs; }

protected:
	bool OpenTxv(const std::string& url);
	void HandleConnect(const boost::system::error_code& error, boost::asio::ip::tcp::resolver::iterator i);
	bool OpenStream(const std::string& streamUrl);
	bool GrabFrameTxv();
	bool GrabFrameStream();

	bool SendGetLiveVideo();
	bool SendGetStoredVideo();
	bool SendPing();
	bool GetBlock();
	void HandleTimeout(const boost::system::error_code& error);
	void HandleRead(const boost::system::error_code& error, size_t bytes);
	bool TcpReadVideoFrame();
	bool OpenCodec();
	void CloseCodec();
	double r2d(AVRational r);

	boost::asio::io_service m_service;
	boost::asio::deadline_timer m_dt;
	boost::asio::ip::tcp::socket m_socket;
	enum { BUF_SIZE = 10 * 1024 };
	char m_buffer[BUF_SIZE];
	volatile size_t m_bytesRead;
	boost::system::error_code m_lastError;
	int m_chunkNr;

	AVCodec*		m_codec;
	AVCodecContext*	m_ctx;
	AVFrame*		m_frame;
	AVPacket		m_avpkt;
	struct SwsContext*	m_imgConvertCtx;
	AVFrame         m_rgbPicture;
	std::vector<unsigned char> m_packetData;

	BufferType		m_received;
	bool			m_decoderInitialized;
	VideoFrame		m_videoFrame;
	unsigned int	m_videoChannel;
	std::string			m_url;
	int				m_frameType;
	TxvVideoFrm		m_txvFrame;
	bool			m_skipGrab;

	AVFormatContext*	m_formatContext;
	int					m_videoStreamNumber;
	AVStream*			m_videoStream;
	unsigned long long	m_frameNumber;

	volatile bool			m_isLive;
	volatile unsigned int	m_chid;
	volatile bool			m_isChidSet;
	std::string				m_host;
	volatile int			m_port;
	volatile int			m_channel;
	volatile unsigned long	m_startTime;
	volatile unsigned int	m_duration;
	volatile unsigned int	m_aliveCounter;
	bool					m_validFrame;
	unsigned int			m_framePeriodMs;
	unsigned long long		m_lastFrameTimestamp;

	typedef std::map<unsigned int, unsigned int> FramePeriods;
	FramePeriods			m_framePeriods; ///< frequency of all frame periods measured	
};

typedef boost::shared_ptr<AidVideo> AidVideoPtr;
