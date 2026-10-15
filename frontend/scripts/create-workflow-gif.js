import { writeFile } from 'node:fs/promises';
import gifenc from 'gifenc';
const { GIFEncoder, quantize, applyPalette } = gifenc;

const width=240,height=160,frames=18;
const colors={bg:[244,249,245,255],white:[255,255,255,255],ink:[44,60,48,255],muted:[147,160,150,255],line:[227,235,228,255],green:[50,133,106,255],mint:[231,244,234,255],soft:[247,249,247,255],amber:[215,175,88,255]};
function frame(index){
  const data=new Uint8Array(width*height*4);
  for(let i=0;i<width*height;i++)data.set(colors.bg,i*4);
  const rect=(x,y,w,h,color,r=0)=>{for(let py=Math.max(0,y);py<Math.min(height,y+h);py++)for(let px=Math.max(0,x);px<Math.min(width,x+w);px++){if(r&&((px<x+r&&py<y+r&&(px-x-r)**2+(py-y-r)**2>r*r)||(px>=x+w-r&&py<y+r&&(px-x-w+r)**2+(py-y-r)**2>r*r)||(px<x+r&&py>=y+h-r&&(px-x-r)**2+(py-y-h+r)**2>r*r)||(px>=x+w-r&&py>=y+h-r&&(px-x-w+r)**2+(py-y-h+r)**2>r*r)))continue;data.set(color,(py*width+px)*4)}};
  const dot=(cx,cy,r,color)=>{for(let y=cy-r;y<=cy+r;y++)for(let x=cx-r;x<=cx+r;x++)if((x-cx)**2+(y-cy)**2<=r*r&&x>=0&&x<width&&y>=0&&y<height)data.set(color,(y*width+x)*4)};
  rect(22,18,196,124,colors.white,12);rect(22,18,196,29,colors.soft,12);rect(34,30,7,7,[232,184,169,255],4);rect(46,30,7,7,[226,201,148,255],4);rect(58,30,7,7,[132,190,148,255],4);rect(22,47,196,1,colors.line);
  rect(37,59,20,20,colors.mint,6);rect(64,63,74,4,colors.ink,2);rect(64,71,52,3,colors.muted,2);
  for(let row=0;row<3;row++){const y=91+row*17;rect(36,y,168,1,colors.line);dot(43,y+9,4,row===0?colors.green:row===1?colors.amber:colors.muted);rect(53,y+5,54,3,colors.ink,1);rect(53,y+11,38,2,colors.muted,1);rect(162,y+5,30,6,row===0?colors.mint:colors.soft,3);}
  const progress=19+Math.round((index/(frames-1))*48);rect(36,133,168,3,colors.line,2);rect(36,133,progress,3,colors.green,2);
  const glow=index<9?index:17-index;dot(190,33,2+Math.floor(glow/3),[50,133,106,170]);return data;
}
const gif=GIFEncoder({auto:true});
for(let i=0;i<frames;i++){const data=frame(i),palette=quantize(data,32);gif.writeFrame(applyPalette(data,palette),width,height,{palette,delay:90,repeat:0});}
gif.finish();await writeFile(new URL('../public/illustrations/task-flow.gif',import.meta.url),gif.bytes());
