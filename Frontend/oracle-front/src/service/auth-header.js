export default function authHeader() {
  const token = JSON.parse(localStorage.getItem('authToken'));
  if (!token) {
    throw new Error('No token found');
    return {};
  }
  else {
    
    return { 
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
      // 'Access-Control-Allow-Origin': '*',
      // 'Access-Control-Allow-Headers': 'X-Requested-With'
    }; // for Spring Boot back-end
    //return { 'x-access-token': user.accessToken };       // for Node.js Express back-end

  } 
}
