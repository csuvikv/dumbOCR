function minboundbox(I)
I = imread('figure-65.png'); 
I = im2bw(I);
corners = detectMinEigenFeatures(I); 
strong = corners.selectStrongest(1000); 
figure, 
imshow(I); 
hold on 
plot(strong)
coor = strong.Location ;
xmin = min(coor(:,1)) ; xmax = max(coor(:,1));
ymin = min(coor(:,2)) ; ymax = max(coor(:,2));
% Rectangle coordinates
O = [xmin,ymin ; xmax ymin ; xmax ymax ; xmin ymax ; xmin ymin] ;
plot(O(:,1),O(:,2),'k')
end