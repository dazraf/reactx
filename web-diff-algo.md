
# 1. No change

```
[ ] -> [ ] => [ ]
```

# 2. Append a vnode to the end of the list of children

```
[ ] -> [ v1 ] => 

[ 
    insertBefore(v1(), null) 
]
```   

# 3. Insert vnode before another vnode

```
[ n1 ] -> [ v2, v1 ] =>  
[ 
    replace(v2(), n1), 
    insertBefore(v1(), null)
]
```

# 4. Insert vnode before another vnode, with keys

```
[ nk1 ] -> [ vk2, vk1 ] =>  
[ 
    insertBefore(vk2(), nk1)
]
```

# 5. Delete a vnode

```
[ n1, n2, n3 ] -> [ v1, v3 ] =>  
[ 
    replace(v3(), n2)
    delete(n3)
]
```

# 6. Delete a vnode, with keys

```
[ nk1, nk2, nk3 ] -> [ vk1, vk3 ] =>  
[ 
    delete(nk2)
]
```
