package model.interactionClass;

import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAinteger16LE;
import hla.rti1516e.exceptions.RTIinternalError;
import skf.coder.Coder;

public class MTRModeCoder implements Coder<MTRMode> {
	
	private HLAinteger16LE coder = null;
	private EncoderFactory factory = null;
	
	public MTRModeCoder() throws RTIinternalError {
		this.factory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
		this.coder = factory.createHLAinteger16LE();
	}

	@Override
	public MTRMode decode(byte[] code) throws DecoderException {
		coder.decode(code);
		return MTRMode.lookup(coder.getValue());
	}

	@Override
	public byte[] encode(MTRMode element) {
		coder.setValue(element.getValue());
		return coder.toByteArray();
	}

	@Override
	public Class<MTRMode> getAllowedType() {
		return MTRMode.class;
	}

}
