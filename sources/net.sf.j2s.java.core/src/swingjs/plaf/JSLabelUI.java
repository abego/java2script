package swingjs.plaf;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.LookAndFeel;

import swingjs.api.js.DOMNode;

/**
 * A JavaScript equivalent for a label.
 * 
 *   Also used for ToolTip
 * 
 * @author Bob Hanson
 *
 */
public class JSLabelUI extends JSLightweightUI {
	protected int gap;
	protected String text;

	public JSLabelUI() {
		setDoc();
		isLabel = true;
	}

	@Override
	public DOMNode updateDOMNode() {
		if (domNode == null) {
			domNode = newDOMObject("label", id);
			textNode = iconNode = null;
			// labels are different from buttons, because we allow them to have
			// different centerings - left, top, middle, bottom, etc.
			addCentering(domNode);
		}
		getIconAndText(); // could be ToolTip
		setIconAndText("label", icon, gap, text);
		DOMNode.setStyles(domNode, "position", "absolute", "width", c.getWidth()
				+ "px", "height", c.getHeight() + "px");
		updateCenteringNode();
		if (allowTextAlignment) {
			// not for JToolTip
			setAlignments((AbstractButton) (JComponent) label);
		}
		if (jc.isEnabled())
			setBackground(jc.isOpaque() ? jc.getBackground() : null);
		return updateDOMNodeCUI();
	}

	protected void getIconAndText() {	
		// overridden in JSToolTipUI
		label = (JLabel) jc;
		icon = label.getIcon();
		gap = label.getIconTextGap();
		text = label.getText();
	}


	@Override
	public void installUI(JComponent jc) {
		label = (JLabel) jc;
    LookAndFeel.installColorsAndFont(jc, "Label.background", "Label.foreground",
        "Label.font");
	}

	@Override
	public void uninstallUI(JComponent jc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dimension getMaximumSize(JComponent jc) {
		return getPreferredSize(jc);
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
		// TODO: implement this for buttons?
		DOMNode.setStyles(textNode, "overflow", "hidden", "white-space", "nowrap");
		if (icon != null) {
			// The graphics object is translated to the label, 
			// not the image, at this point. In order to get
			// a clientRectangle, the node must be visible, even for just
			// an instant.
			DOMNode.setStyles(imageNode, "visibility", null);
			Rectangle r = imageNode.getBoundingClientRect();
			DOMNode parent = null;
			boolean isHidden = (r.width == 0);
			if (isHidden) {
				parent = DOMNode.getParent(domNode);
				$("body").append(domNode);
				r = imageNode.getBoundingClientRect();
			}
			Rectangle r0 = domNode.getBoundingClientRect();
			if (isHidden)
				DOMNode.transferTo(domNode,  parent);				
			DOMNode.setStyles(imageNode, "visibility", "hidden");
			icon.paintIcon(c, g, (int) (r.x - r0.x), (int) (r.y - r0.y));
		}
	}

	@Override
	public Dimension getPreferredSize(JComponent jc) {
		return (label == null ? super.getPreferredSize(jc) : JSGraphicsUtils.getPreferredButtonSize(((AbstractButton) jc), ((AbstractButton) jc).getIconTextGap()));
	}

}
